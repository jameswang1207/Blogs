# Department

```
    department {
            _id                 ObjectId
            name                部门名称
            number              部门编号
            roles [ roleId, roleId] ObjectId角色id
            parentId            ObjectId上级部门标识
            level               部门等级
            describe            部门描述
            isDeleted           是否删除
            createdAt           创建时间
            updatedAt           更新时间
    }
```

# Position

```
    Position {                  职位
            _id                 ObjectId
            name                职位名称
            type                职位类别
            describe            职位描述
            zhiJi {             职级
                name            职级名称
                number          职级编号
            }
            zhiDen {            职等
                name            职等名称
                number          职等编号
            }
            zhiXi {             职系
                name            职系名称
                number          职系编号
            }
            zhiZu {             职组
                name            职组名称
                number          职组编号
                zhiMen {        职门
                     name       职门名称
                     number     职门编号
                }
            }
            isDeleted           是否删除
            createdAt           创建时间
            updatedAt           更新时间
    }
```

# Post

```
    Post {
            _id                    ObjectId
            name                   岗位名称
            type                   岗位类别
            roles [roleId , roleId] ObjectId角色id
            parentId               ObjectId上级岗位标识
            department             ObjectId部门标识
            positions [ positionId ] ObjectId职位标识
            describe               岗位描述
            isDeleted              是否删除
            createdAt              创建时间
            updatedAt              更新时间
    }
```

# UserProperties
```
    userProperty {
        _id                 ObjectId
        order               属性的展示顺序(1-n)
        name                属性的名称
        number              编号
        type                属性的类型('single-text','multiple-text','date','single-select','multiple-select')
        根据类型不同可以扩展新的属性
        validate             正则表达式校验
        isRequired           true or false
        isUnique             true or false
        isVisible            true or false
        isDeleted            是否删除
        createdAt            创建时间
        updatedAt            更新时间
    }
```

# User

```
    user {
        _id                    ObjectId
        avatar                 用户头像
        name                   用户名字
        post [postId, postId]  岗位标识:填写ObjectId
        tags [tagId, tagId ]   标签标识:填写ObjectId
        role [roleId,roleId]   角色id
        properties [           用户扩展属性
            {id: ObjectId, value: String},
            {id: ObjectId, value: String}
            ...
        ]
        identityId {
            id: ObjectId,      唯一标识
            url: String,       验证地址
            source：appId,     来源系统
            name: name,        身份id名称
            protocol: protocol, 协议
            requestParmeter: requests, 输入参数
            responseParmeter: response 响应参数
        }
        isDeleted             是否删除
        createdAt             创建时间
        updatedAt             更新时间
    }
```

# Tag

```
    tag {
        _id                   ObjectId
        groupId               ObjectId
        parentId              ObjectId上级tag标识
        appId                 接入应用id
        roleId                roleId
        tag                   标签名称
        isDeleted             是否删除
        createdAt             创建时间
        updatedAt             更新时间
    }
```

