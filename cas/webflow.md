### cas webflow 解析

````java
package org.springframework.webflow.engine;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.binding.mapping.Mapper;
import org.springframework.binding.mapping.MappingResults;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.style.StylerUtils;
import org.springframework.core.style.ToStringCreator;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.webflow.core.AnnotatedObject;
import org.springframework.webflow.core.collection.AttributeMap;
import org.springframework.webflow.core.collection.MutableAttributeMap;
import org.springframework.webflow.definition.FlowDefinition;
import org.springframework.webflow.definition.StateDefinition;
import org.springframework.webflow.definition.TransitionDefinition;
import org.springframework.webflow.execution.FlowExecutionException;
import org.springframework.webflow.execution.RequestContext;

public class Flow extends AnnotatedObject implements FlowDefinition {

    /**
     * Logger, can be used in subclasses.
     */
    protected final Log logger = LogFactory.getLog(getClass());

    /**
     * An assigned flow identifier uniquely identifying this flow among all other flows.
     */
    private String id;

    /**
     * The set of state definitions for this flow.
     */
    private Set<State> states = new LinkedHashSet<>(9);

    /**
     * 对于这个流程开始时第一个执行的节点
     */
    private State startState;

    /**
     * The set of flow variables created by this flow.
     */
    private Map<String, FlowVariable> variables = new LinkedHashMap<>();

    /**
     * The mapper to map flow input attributes.
     */
    private Mapper inputMapper;

    /**
     * 流程开始之前需要执行的节点列表
     */
    private ActionList startActionList = new ActionList();

    /**
     * The set of global transitions that are shared by all states of this flow.
     */
    private TransitionSet globalTransitionSet = new TransitionSet();

    /**
     * The list of actions to execute when this flow ends.
     */
    private ActionList endActionList = new ActionList();

    /**
     * The mapper to map flow output attributes.
     */
    private Mapper outputMapper;

    /**
     * The set of exception handlers for this flow.
     */
    private FlowExecutionExceptionHandlerSet exceptionHandlerSet = new FlowExecutionExceptionHandlerSet();

    /**
     * An optional application context hosting services needed by this flow.
     */
    private ApplicationContext applicationContext;

    /**
     * Construct a new flow definition with the given id. The id should be unique among all flows.
     * @param id the flow identifier
     */
    public Flow(String id) {
        Assert.hasText(id, "This flow must be uniquely identified");
        this.id = id;
    }

    // convenient static factory methods

    /**
     * Create a new flow with the given id and attributes.
     * @param id the flow id
     * @param attributes the attributes
     * @return the flow
     */
    public static Flow create(String id, AttributeMap<?> attributes) {
        Flow flow = new Flow(id);
        flow.getAttributes().putAll(attributes);
        return flow;
    }

    // implementing FlowDefinition

    public String getId() {
        return id;
    }

    public StateDefinition getStartState() {
        if (startState == null) {
            throw new IllegalStateException("No start state has been set for this flow ('" + getId()
                    + "') -- flow builder configuration error?");
        }
        return startState;
    }

    public StateDefinition getState(String stateId) {
        return getStateInstance(stateId);
    }

    public String[] getPossibleOutcomes() {
        List<String> possibleOutcomes = new ArrayList<>();
        for (State state : states) {
            if (state instanceof EndState) {
                possibleOutcomes.add(state.getId());
            }
        }
        return possibleOutcomes.toArray(new String[possibleOutcomes.size()]);
    }

    public ClassLoader getClassLoader() {
        if (applicationContext != null) {
            return applicationContext.getClassLoader();
        } else {
            return ClassUtils.getDefaultClassLoader();
        }
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public boolean inDevelopment() {
        return getAttributes().getBoolean("development", false);
    }

    /**
     * Add given state definition to this flow definition. Marked protected, as this method is to be called by the
     * (privileged) state definition classes themselves during state construction as part of a FlowBuilder invocation.
     * @param state the state to add
     * @throws IllegalArgumentException when the state cannot be added to the flow; for instance if another state shares
     * the same id as the one provided or if given state already belongs to another flow
     */
    protected void add(State state) throws IllegalArgumentException {
        if (this != state.getFlow() && state.getFlow() != null) {
            throw new IllegalArgumentException("State " + state + " cannot be added to this flow '" + getId()
                    + "' -- it already belongs to a different flow: '" + state.getFlow().getId() + "'");
        }
        if (this.states.contains(state) || this.containsState(state.getId())) {
            throw new IllegalArgumentException("This flow '" + getId() + "' already contains a state with id '"
                    + state.getId() + "' -- state ids must be locally unique to the flow definition; "
                    + "existing state-ids of this flow include: " + StylerUtils.style(getStateIds()));
        }
        boolean firstAdd = states.isEmpty();
        states.add(state);
        if (firstAdd) {
            setStartState(state);
        }
    }

    /**
     * Returns the number of states defined in this flow.
     * @return the state count
     */
    public int getStateCount() {
        return states.size();
    }

    /**
     * Is a state with the provided id present in this flow?
     * @param stateId the state id
     * @return true if yes, false otherwise
     */
    public boolean containsState(String stateId) {
        for (State state : states) {
            if (state.getId().equals(stateId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Set the start state for this flow to the state with the provided <code>stateId</code>; a state must exist by the
     * provided <code>stateId</code>.
     * @param stateId the id of the new start state
     * @throws IllegalArgumentException when no state exists with the id you provided
     */
    public void setStartState(String stateId) throws IllegalArgumentException {
        setStartState(getStateInstance(stateId));
    }

    /**
     * Set the start state for this flow to the state provided; any state may be the start state.
     * @param state the new start state
     * @throws IllegalArgumentException given state has not been added to this flow
     */
    public void setStartState(State state) throws IllegalArgumentException {
        if (!states.contains(state)) {
            throw new IllegalArgumentException("State '" + state + "' is not a state of flow '" + getId() + "'");
        }
        startState = state;
    }

    /**
     * Return the <code>TransitionableState</code> with given <code>stateId</code>.
     * @param stateId id of the state to look up
     * @return the transitionable state
     * @throws IllegalArgumentException if the identified state cannot be found
     * @throws ClassCastException when the identified state is not transitionable
     */
    public TransitionableState getTransitionableState(String stateId) throws IllegalArgumentException,
            ClassCastException {
        State state = getStateInstance(stateId);
        if (state != null && !(state instanceof TransitionableState)) {
            throw new ClassCastException("The state '" + stateId + "' of flow '" + getId() + "' must be transitionable");
        }
        return (TransitionableState) state;
    }

    /**
     * Lookup the identified state instance of this flow.
     * @param stateId the state id
     * @return the state
     * @throws IllegalArgumentException if the identified state cannot be found
     */
    public State getStateInstance(String stateId) throws IllegalArgumentException {
        if (!StringUtils.hasText(stateId)) {
            throw new IllegalArgumentException("The specified stateId is invalid: state identifiers must be non-blank");
        }
        for (State state : states) {
            if (state.getId().equals(stateId)) {
                return state;
            }
        }
        throw new IllegalArgumentException("Cannot find state with id '" + stateId + "' in flow '" + getId() + "' -- "
                + "Known state ids are '" + StylerUtils.style(getStateIds()) + "'");
    }

    /**
     * Convenience accessor that returns an ordered array of the String <code>ids</code> for the state definitions
     * associated with this flow definition.
     * @return the state ids
     */
    public String[] getStateIds() {
        String[] stateIds = new String[getStateCount()];
        int i = 0;
        for (State state : states) {
            stateIds[i++] = state.getId();
        }
        return stateIds;
    }

    /**
     * Adds a flow variable.
     * @param variable the variable
     */
    public void addVariable(FlowVariable variable) {
        variables.put(variable.getName(), variable);
    }

    /**
     * Adds flow variables.
     * @param variables the variables
     */
    public void addVariables(FlowVariable... variables) {
        if (variables == null) {
            return;
        }
        for (FlowVariable variable : variables) {
            addVariable(variable);
        }
    }

    /**
     * Returns the flow variable with the given name.
     * @param name the name of the variable
     */
    public FlowVariable getVariable(String name) {
        return variables.get(name);
    }

    /**
     * Returns the flow variables.
     */
    public FlowVariable[] getVariables() {
        return variables.values().toArray(new FlowVariable[variables.size()]);
    }

    /**
     * Returns the configured flow input mapper, or null if none.
     * @return the input mapper
     */
    public Mapper getInputMapper() {
        return inputMapper;
    }

    /**
     * Sets the mapper to map flow input attributes.
     * @param inputMapper the input mapper
     */
    public void setInputMapper(Mapper inputMapper) {
        this.inputMapper = inputMapper;
    }

    /**
     * Returns the list of actions executed by this flow when an execution of the flow <i>starts</i>. The returned list
     * is mutable.
     * @return the start action list
     */
    public ActionList getStartActionList() {
        return startActionList;
    }

    /**
     * Returns the list of actions executed by this flow when an execution of the flow <i>ends</i>. The returned list is
     * mutable.
     * @return the end action list
     */
    public ActionList getEndActionList() {
        return endActionList;
    }

    /**
     * Returns the configured flow output mapper, or null if none.
     * @return the output mapper
     */
    public Mapper getOutputMapper() {
        return outputMapper;
    }

    /**
     * Sets the mapper to map flow output attributes.
     * @param outputMapper the output mapper
     */
    public void setOutputMapper(Mapper outputMapper) {
        this.outputMapper = outputMapper;
    }

    /**
     * Returns the set of exception handlers, allowing manipulation of how exceptions are handled when thrown during
     * flow execution. Exception handlers are invoked when an exception occurs at execution time and can execute custom
     * exception handling logic as well as select an error view to display. Exception handlers attached at the flow
     * level have an opportunity to handle exceptions that aren't handled at the state level.
     * @return the exception handler set
     */
    public FlowExecutionExceptionHandlerSet getExceptionHandlerSet() {
        return exceptionHandlerSet;
    }

    /**
     * Returns the set of transitions eligible for execution by this flow if no state-level transition is matched. The
     * returned set is mutable.
     * @return the global transition set
     */
    public TransitionSet getGlobalTransitionSet() {
        return globalTransitionSet;
    }

    /**
     * Returns the transition that matches the event with the provided id.
     * @param eventId the event id
     * @return the transition that matches, or null if no match is found.
     */
    public TransitionDefinition getGlobalTransition(String eventId) {
        for (Transition transition : globalTransitionSet) {
            if (transition.getId().equals(eventId)) {
                return transition;
            }
        }
        return null;
    }

    /**
     * Sets a reference to the application context hosting application objects needed by this flow.
     * @param applicationContext the application context
     */
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    // id based equality

    public boolean equals(Object o) {
        if (!(o instanceof Flow)) {
            return false;
        }
        Flow other = (Flow) o;
        return id.equals(other.id);
    }

    public int hashCode() {
        return id.hashCode();
    }

    // behavioral code, could be overridden in subclasses

    /**
     * Start a new session for this flow in its start state. This boils down to the following:
     * <ol>
     * <li>Create (setup) all registered flow variables ({@link #addVariable(FlowVariable)}) in flow scope.</li>
     * <li>Map provided input data into the flow. Typically data will be mapped into flow scope using the registered
     * input mapper ({@link #setInputMapper(Mapper)}).</li>
     * <li>Execute all registered start actions ( {@link #getStartActionList()}).</li>
     * <li>Enter the configured start state ({@link #setStartState(State)})</li>
     * </ol>
     * @param context the flow execution control context
     * @param input eligible input into the session
     * @throws FlowExecutionException when an exception occurs starting the flow
     */
    public void start(RequestControlContext context, MutableAttributeMap<?> input) throws FlowExecutionException {
        assertStartStateSet();
        createVariables(context);
        if (inputMapper != null) {
            MappingResults results = inputMapper.map(input, context);
            if (results != null && results.hasErrorResults()) {
                throw new FlowInputMappingException(getId(), results);
            }
        }
        startActionList.execute(context);
        startState.enter(context);
    }

    /**
     * Resume a paused session for this flow in its current view state.
     * @param context the flow execution control context
     * @throws FlowExecutionException when an exception occurs during the resume operation
     */
    public void resume(RequestControlContext context) throws FlowExecutionException {
        restoreVariables(context);
        getCurrentViewState(context).resume(context);
    }

    /**
     * Handle the last event that occurred against an active session of this flow.
     * @param context the flow execution control context
     */
    public boolean handleEvent(RequestControlContext context) {
        TransitionableState currentState = getCurrentTransitionableState(context);
        try {
            return currentState.handleEvent(context);
        } catch (NoMatchingTransitionException e) {
            // try the flow level transition set for a match
            Transition transition = globalTransitionSet.getTransition(context);
            if (transition != null) {
                return context.execute(transition);
                // return transition.execute(currentState, context);
            } else {
                // no matching global transition => let the original exception
                // propagate
                throw e;
            }
        }
    }

    /**
     * Inform this flow definition that an execution session of itself has ended. As a result, the flow will do the
     * following:
     * <ol>
     * <li>Execute all registered end actions ({@link #getEndActionList()}).</li>
     * <li>Map data available in the flow execution control context into provided output map using a registered output
     * mapper ( {@link #setOutputMapper(Mapper)}).</li>
     * </ol>
     * @param context the flow execution control context
     * @param outcome the logical flow outcome that will be returned by the session, generally the id of the terminating
     * end state
     * @param output initial output produced by the session that is eligible for modification by this method
     * @throws FlowExecutionException when an exception occurs ending this flow
     */
    public void end(RequestControlContext context, String outcome, MutableAttributeMap<?> output)
            throws FlowExecutionException {
        endActionList.execute(context);
        if (outputMapper != null) {
            MappingResults results = outputMapper.map(context, output);
            if (results != null && results.hasErrorResults()) {
                throw new FlowOutputMappingException(getId(), results);
            }
        }
    }

    public void destroy() {
        if (applicationContext != null && applicationContext instanceof ConfigurableApplicationContext) {
            ((ConfigurableApplicationContext) applicationContext).close();
        }
    }

    /**
     * Handle an exception that occurred during an execution of this flow.
     * @param exception the exception that occurred
     * @param context the flow execution control context
     */
    public boolean handleException(FlowExecutionException exception, RequestControlContext context)
            throws FlowExecutionException {
        return getExceptionHandlerSet().handleException(exception, context);
    }

    // internal helpers

    private void assertStartStateSet() {
        if (startState == null) {
            throw new IllegalStateException("Unable to start flow '" + id
                    + "'; the start state is not set -- flow builder configuration error?");
        }
    }

    private void createVariables(RequestContext context) {
        for (FlowVariable variable : variables.values()) {
            if (logger.isDebugEnabled()) {
                logger.debug("Creating " + variable);
            }
            variable.create(context);
        }
    }

    public void restoreVariables(RequestContext context) {
        for (FlowVariable variable : variables.values()) {
            if (logger.isDebugEnabled()) {
                logger.debug("Restoring " + variable);
            }
            variable.restore(context);
        }
    }

    private ViewState getCurrentViewState(RequestControlContext context) {
        State currentState = (State) context.getCurrentState();
        if (!(currentState instanceof ViewState)) {
            throw new IllegalStateException("You can only resume paused view states, and state "
                    + context.getCurrentState() + " is not a view state - programmer error");
        }
        return (ViewState) currentState;
    }

    private TransitionableState getCurrentTransitionableState(RequestControlContext context) {
        State currentState = (State) context.getCurrentState();
        if (!(currentState instanceof TransitionableState)) {
            throw new IllegalStateException("You can only signal events in transitionable states, and state "
                    + context.getCurrentState() + " is not transitionable - programmer error");
        }
        return (TransitionableState) currentState;
    }

    public String toString() {
        return new ToStringCreator(this).append("id", id).append("states", states).append("startState", startState)
                .append("variables", variables).append("inputMapper", inputMapper)
                .append("startActionList", startActionList).append("exceptionHandlerSet", exceptionHandlerSet)
                .append("globalTransitionSet", globalTransitionSet).append("endActionList", endActionList)
                .append("outputMapper", outputMapper).toString();
    }
}
```