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

# Display user image（回显用户照片）
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

#  One To N authentication(人脸1:n认证)
- url: /faceid/api/auth/search?token=xxxx
- method: post
- header: Content-Type: application/json
- parmeters

| image | feature | appId |
|------|----------|------------|
| 图片base64 | 特征值，如果有图片就不用上传特征值 | 应用厂商后端appid |

-  response
```json
{
  "code": 200,
  "message": "OK",
  "data":{
    "userId": "20121016",
    "image": "/group1/M00/00/02/xxx.jpg",
    "score": 0.4596883
  }
}
```

# 人脸上传对比1:N
- url: /faceid/api/auth/upload/search?token=xxxx
- method: post
- header:  Content-Type: multipart/form-data
- parmeters: 注意这是from提交

| file | appId |
|------|----------|
| 文件 | 应用厂商后端appid |

- response
```json
{
  "code": 200,
  "message": "OK",
  "data":{
    "userId": "20121004",
    "image": "/group1/M00/00/02/xxxxxjpg",
    "score": 0.99885213
  }
}
```

# 人脸比对1:1
- url：/faceid/api/auth/upload/compare?token=xxxx
- method: post
- header: Content-Type: application/json
- parmeters: 参数组合说明: imageA | imageB , imageA | featureB, imageA | featureB, featureA | featureB 

| appId | imageA | imageB | featureA | featureB |
|-------|--------|--------|----------|----------|
| 应用厂商后端appid | 图片A | 图片B | 图片A的特征值 | 图片B的特征值 |

- response:

```json
{
  "code": 200,
  "message": "OK",
  "data":{
  "score": 0.3272
  }
}
```

# 上传两张图片做比较1:1
- url：/faceid/api/auth/upload/compare?token=xxxx
- method: post
- header: Content-Type: multipart/form-data

- parmeters:

| appId | imageA | imageB |
|-------|--------|--------|
| 应用厂商后端appid | 图片A |图片B|

- response:
```json
{
    "code": 200,
    "message": "OK",
    "data":{
        "score": 0.3775
    }
}
```


# 实现人证1:1
- url：/faceid/api/auth/compare/upload/userId?token=xxxx
- method: post
- header: Content-Type: multipart/form-data

- parmeters:

| appId | userId | imgB |
|-------|--------|--------|
| 应用厂商后端appid | 用户userId |图片B|

- response:
```json
{
"code": 200,
"message": "OK",
    "data":{
        "userId": "19891063",
        "image": "/group1/M00/00/02/rB8YPF0dyc2AS_gEAAB0fgS4DWY972.jpg",
        "score": 0.3451
    }
}
```



# 实现人证1:1
- url：faceid/api/auth/compare/userId?token=xxxx
- method: post
- header: Content-Type: application/json

- parmeters:

| appId | userId | featureB | imgB |
|-------|--------|--------|
| 应用厂商后端appid | 用户userId | 图片B特征值 | 照片Bbase64 |

- response:
```json
{
"code": 200,
"message": "OK",
    "data":{
        "userId": "19891063",
        "image": "/group1/M00/00/02/rB8YPF0dyc2AS_gEAAB0fgS4DWY972.jpg",
        "score": 0.3451
    }
}
```


#  用户上传照片照片base64
- url：faceid/api/face/image?token=xxx
- method: post
- header: Content-Type: application/json
- parmeters:

| userId | image | type |
|-------|--------|--------|
| 用户工号 | 用户照片base64 | 01 |


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

# 用户上传照片
- url：/faceid/api/face/upload?token=xxxx
- method: post
- header: Content-Type: multipart/form-data
- parmeters:

| userId | file | type |
|-------|--------|--------|
| 用户工号 | 照片后缀为用户id | 01 |

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
- url: /faceid/api/face/findUserFace?token=xx
- method: post
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
- url: /faceid/api/face/upload/quality?token=xx
- method: post
- header: header: multipart/form-data
- paramter:

| appId | file |
|-------| ----------- |
| 应用厂商前端id | 照片 |

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

# 获取照片质量
- url: faceid/api/face/quality?token=xx
- method: post
- header: header: application/json
- paramter:

| appId | image |
|-------| ----------- |
| 应用厂商前端id | 照片质量base64 |

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








