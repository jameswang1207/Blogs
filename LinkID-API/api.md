# 响应中的code值说明
- HTTP status codes
- 请求对照http请求头code

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

```
# Get token
- 统一身份平台(域名有学校提供)
- url: /oauth2.0/accessToken?grant_type=client_credentials&client_id=xx&client_secret=xxx
- method: GET
- header: Content-Type: application/json

- parameters: no

- Ok response
```json
{
"access_token": "xxxxxx",
"token_type": "bearer",
"expires_in": 28800
}
```

# Display user image
- 域名为人脸服务域名或ip
- url: /group1/M00/00/02/rB8YPF0ZyfOAXEgFAAC2p-Q3Nlw565.jpg?token=xxxx
- method: get
- parameters: no
- OK response: 照片直接显示
- Fail response:
```json
{
"code": 412,
"data":{
  "errorCode": 8000014,
  "errorMessage": "Request rejection, please vaidate application's credential"
  },
 "message": "Precondition Failed"
}
```
