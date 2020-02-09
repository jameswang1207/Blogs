## 人脸比对1:n
### url: /api/face/search
### method: post
### header: Content-Type: application/json
### parmeters:
```json
{
    "image": "xxx", //图片base64
    "feature":"xxxx", // 特征值，如果有图片就不用上传特征值
    "factoryId":"5d09ccc201c75939a35e2505", // 厂商id
    "appId":"e31c68be-689e-433e-beaf-6d6aad08a2d8" // 厂商应用
}
```
### response:
```json
{
    "code": 200,
    "message": "OK",
    "data":{
        "userId": "HD001",
        "image":"http:///xxxx",
        "score": 0.9992053
    }
}
```

## 人脸上传对比
### url: /api/face/upload/search
### method: post
### header:  Content-Type: multipart/form-data
### parmeters: 注意这是from提交
```json
    file: 文件
    factoryId：厂商id
    appId: 应用id
```
### response
```json
{
    "code": 200,
    "message": "OK",
    "data":{
        "userId": "YT00563",
        "image":"http:///xxxx",
        "score": 0.9990221
    }
}
```

## 人脸比对1:1
### url：/api/face/compare
### method: post
### header: Content-Type: application/json
### parmeters:
```json: 必须成对出现
# 组合方式：imageA | imageB , imageA | featureB, imageA | featureB, featureA | featureB 
{
    "factoryId": "", // 厂商id
    "appId": "", //应用id
    "imageA": "", // 图片A
    "imageB": "", // 图片B
    "featureA" : "", // 图片a的特征值
    "featureB" : "" // 图片B的特征值
}
```
### response:
```json
{
    "code": 200,
    "message": "OK",
    "data":{
        "score": 0.9992
    }
}
```

## 上传两张图片做比较
### url：/api/face/upload/compare
### method: post
### header: Content-Type: multipart/form-data
### parmeters:
```json
    imgA: 图片1,
    imgB: 图片2,
    factoryId：厂商id,
    appId: 应用id
```
### response:
```json
{
    "code": 200,
    "message": "OK",
    "data":{
        "score": 0.3775
    }
}
```

###  用户上传照片照片base64
### url：/api/face/image
### method: post
### header: Content-Type: application/json
### parmeters:
```json
{
  "key":"01", // 存储类型
  "userId":"xxx", 用户工号
  "factoryId":"5d09ccc201c75939a35e2505", //  厂商id
  "image":"xxxx", // image base63
  "appId":"e31c68be-689e-433e-beaf-6d6aad08a2d8", // 应用id
  "type":"01" // 照片类型
}
```
### response：
```json
{
    "code": 200,
    "message": "OK",
    "data": true
}
```


###  用户上传照片
### url：/api/face/image
### method: post
### header: Content-Type: multipart/form-data
### parmeters:
```json
  key:"01", // 存储类型
  userId:"xxx", 用户工号
  factoryId:"5d09ccc201c75939a35e2505", //  厂商id
  file:xxxx,
  appId:"e31c68be-689e-433e-beaf-6d6aad08a2d8", // 应用id
  type:"01" // 照片类型
```
### response：
```json
{
    "code": 200,
    "message": "OK",
    "data": true
}
```
