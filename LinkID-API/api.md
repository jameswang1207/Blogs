# 响应中的code值说明
- HTTP status codes
- 请求对照http响应头code

# 错误信息和错误码对照表
| errorCode | errorMessage | description |
|------|----------|------------|
| 10001 | No user found for conditions | No user found for user query condition. |
| 10002 | No apps found for conditions | No apps found for apps query condition. |

# Response type

## OK response body
```
{
    "code": "200",
    "message": "OK",
    "data": {
        "name": "姓名",
        "nikename":"昵称"
    }
}
```

## No content response body (No content for query/search request)

```
    {
        "code": 204,
        "message": "No Content",
        "data": null
    }
```

## Error response body (Internal Server Error, wtih Exception Stack Overflow)

```
{
    "code": 500,
    "message": "Internal Server Error",
    "data": {
        "cause": "Exception in thread main java.lang.NullPointerException at se.citerus.dddsample.Main.main(Main.java: 6)"
    }
}
```

## Warning response body (Precondition validate failed)

```
{
    "code": 412,
    "message": "Precondition Failed",
    "data": {
        "errorCode": 100001,
        "errorMessage": "No user found for conditions.",
        "details": [
            "Required field userid not found."
        ]
    }
}
```


# Get user information

- 通过学号或工号或身份证号查询用户信息
- Request Method:
POST

- Request url:
http://{server-domain}/api/identify/getuser

- Request Parameters:

| Name | Type | Required | Example |
| ---- | ---- | -------- | ------- |
| userid | string | YES | 学号、工号、身份证号|

- OK response body

```
{
    "code": "200",
    "message": "OK",
    "data": {
        "JCTB020101.XM ": "姓名",
        "JCTB020102.YWXM": "英文姓名",
        "JCTB020103.XMPY": "姓名拼音",
        "JCTB020104.CYM": "曾用名",
        "JCTB020105.XBM": "性别码",
        "JCTB020106.CSRQ": "出生日期",
        "JCTB020107.CSDM": "出生地码",
        "JCTB020108.JG": "籍贯",
        "JCTB020109.MZM": "民族码",
        "JCTB020110.GJDQM": "国籍/地区",
        "JCTB020111.SFZJLXM": "身份证件类",
        "JCTB020112.SFZJH": "身份证件号",
        "JCTB020113.HYZKM": "婚姻状况码",
        "JCTB020114.GATQWM": "港澳台侨外",
        "JCTB020115.ZZMMM": "政治面貌码",
        "JCTB020116.JKZKM": "健康状况码",
        "JCTB020117.XYZJM": "信仰宗教码",
        "JCTB020118.XXM": "血型码",
        "JCTB020119.ZP": "照片",
        "JCTB020120.RYH": "人员号",
        "JCTB020121.SFZJYXQ": "身份证件有",
        "JCTB020122.SFDSZN": "是否独生子"
    }
}
```

# Get application information

- 通过用户表示和发起调用请求的应用id获取应用列表
- Request Method:
POST

- Request url:
http://{server-domain}/api/app/getapps

- Request Parameters:

| Name | Type | Required | Example |
| ---- | ---- | -------- | ------- |
| appid | string | YES | 发起调用请求的应用id|
| userid | string | YES | 学号或工号或身份证号|

- OK response body

```
{
    "code": "200",
    "message": "OK",
    "data": [{
            "name": "应用名称1",
            "url": "应用链接1",
            "logo": "应用logo链接1"
        },
        {
            "name": "应用名称2",
            "url": "应用链接2",
            "logo": "应用logo链接2"
        }
    ]
}
```
