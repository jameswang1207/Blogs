# 响应中的code值说明
- HTTP status codes
- 请求对照http请求头code

# 错误信息和错误码对照表
| errorCode | errorMessage | description |
|------|----------|------------|
| 10001 | No user found for conditions | No user found for user query condition. |
| 10002 | No apps found for conditions | No apps found for apps query condition. |

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


# Display user image（回显用户照片）--- 三方应用回显照片
- 管理端回显照片使用,在返回的URL中加前缀：/group1/M00/00/02/rB8YPF0ZyfOAXEgFAAC2p-Q3Nlw565.jpg?signature=554f99503594522e5b36159069da572a&accessKey=OC4wNS4wNS4wNy4wMC4wMy4wMS4wMS4w&timestamp=1234556
- 域名为人脸服务域名或ip
- method: get
- parameters: linux生成md5生成势力：md5 -s (security+timestamp)

| signature | accessKey | timestamp |
|------|----------|------------|
| 应用security与时间连接 md5 32位 | 应用appid | 时间戳 |


#  One To N authentication(人脸1:n认证)- demo 认证api
- 需要认证（提供给给他人调用） url： /faceid/public/auth/search
- 无需认证（内部调用） url: /faceid/api/auth/search
- header: Authorization:Bearer token（前面获取到的token）
- method: post
- header: Content-Type: application/json
- parmeters
```json
{
  "appId": "3dff273d-8660-4c53-868d-72c1abcf9097", //认证厂商appid
  "feature": "", //特征值，如果有图片就不用上传特征值，生物特征值于base64方式传递
  "file": {
    "ext": "jpg", // 照片类型
    "image": "" //用户照片base64
  }
}
```

-  response
```json
{
"code": 200,
"message": "OK",
"data":{
    "userId": "20121008",
    "image": "/group1/M00/00/02/rBEIsl0tasGARCpKAACe8jcS8B8677.jpg",
    "score": 0.43955812,
    "appScore": 0.4,
    "hasPass": true,
    "hasPermission": true
  }
}
```
```


# 实现人证1:1 --- demo 认证接口  
- 需要认证（提供给给他人调用）url：faceid/public/auth/compare/userId
- method: post
- header: Authorization:Bearer token（前面获取到的token）
- header: Content-Type: application/json
- parmeters:

```json
{
  "appId": "3dff273d-8660-4c53-868d-72c1abcf9097",  // 认证厂商id
  "featureB": "", // 认证的照片特征值
  "imageB": {
    "ext": "jpg", // 照片类型
    "image": "" // 照片base64
  },
  "userId": "19741062" // 用户工号
}
```

- response:
```json
{
"code": 200,
"message": "OK",
"data":{
    "userId": "20121008",
    "image": "/group1/M00/00/02/rBEIsl0tasGARCpKAACe8jcS8B8677.jpg",
    "score": 0.3508,
    "appScore": 0.4,
    "hasPass": false,
    "hasPermission": true
  }
}
```

#  用户上传照片照片base64
- url：faceid/public/face/image
- method: post
- header: Authorization:Bearer token（前面获取到的token）
- header: Content-Type: application/json
- parmeters:

| userId | image | type | ext|
|-------|--------|--------|--------|
| 用户工号 | 用户照片base64 | 01 | jpg，bmp，png|


- response：
```json
{
"code": 200,
"message": "OK",
    "data":{
        "userId": "20121014",
        "path": "/group1/M00/00/02/rB8YPF0d-kaAPTeJAAH06y22rDA676.jpg"
    }
}
```

# 分页查询用户特征值
- url: /faceid/public/face/findUserFace
- method: post
- header: Authorization:Bearer token（前面获取到的token）
- header: header: Content-Type: application/json
- paramter:

| appId | currentPage | pageSize | timeStamp |
|-------| ----------- | -------- | --------- |
| 应用厂商前端id | 当前页 | 每页多少条 | 如果传输是增量获取， 不传输，全量获取, 是上一次该数据的时间戳 |

### response
```json
{
  "code": 200,
  "message": "OK",
  "data":{
  "pageSize": 1, // 每页多少条
  "pageNum": 1, //当前第几页
  "totalAmount": 22, //总条数
  "results":[
    {
      "userId": "20121013", // 用户id
      "imageUrl": "/group1/M00/00/01/rB8YPF0ZyfKAbW0qAAA9ACU3I1M261.jpg",
      "timeStamp": 1561972900364, // 时间戳
      "deleted": false, // 是否被删除
      "images":
      [
        { 
          "type": "01", // 照片类型，正面照
          "feature": "xxx" // 特征值
        }
      ]
    }
  ]
}
```


# 获取照片质量
- url: faceid/public/face/quality
- method: post
- header: Authorization:Bearer token（前面获取到的token）
- header: header: application/json
- paramter:

```json
{
  "appId": "string", // 厂商应用id F
  "feature": "string", // 照片特征值
  "file": {
    "ext": "string", //照片类型
    "image": "string" //照片base64
  }
}
```

### response
```json
{
"code": 200,
"message": "OK",
    "data":{
        "qualityScores": "xxx ", 质量分数
        "feature": “特征值”
    }
}
```

# 通过用户id及照片类型获取照片
- url：/faceid/public/face/searchFaceByUserIdAndType
- header: Authorization:Bearer token（前面获取到的token）
- method: post
- header: application/json
- parameter:
| userId | type |
|------- |-------|
| 用户工号或学号 | 照片类型|

- response 

```json
# success
{
  "code": 200,
  "message": "OK",
  "data": {
    "userId": "20121018",
    "path": "/group1/M00/00/06/rBEIsl0u4syAdQ6PAAFNi7rfTXI583.jpg"
  }
}

# fail
{
  "code": 204,
  "message": "No Content",
  "data": null
}

```
