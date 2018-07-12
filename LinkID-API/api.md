# Get user information

- Request Method:
POST

- Request Endpoint:
http://{server-domain}/api/identify/getuserbyxh

- Request Parameters:

| Name | Type | Required | Example |
| ---- | ---- | -------- | ------- |
| xh | string | YES | 学号、工号

- Request Example

```
    http://{server-domain}/api/identify/getuserbyxh
```

- Response Example

```
    {
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
```


- Request Method:
POST

- Request Endpoint:
http://{server-domain}/api/identify/getuserbysfzjh

- Request Parameters:

| Name | Type | Required | Example |
| ---- | ---- | -------- | ------- |
| sfzjh | string | YES | 身份证号


- Request Example

```
    http://{server-domain}/api/identify/getuserbysfzjh
```

- Response Example

```
{
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
```

# Get application information

- Request Method:
POST

- Request Endpoint:
http://{server-domain}/api/app/getapps

- Request Parameters:

| Name | Type | Required | Example |
| ---- | ---- | -------- | ------- |
| appid | string | YES | 发起调用平台的id
| userid | string | YES | 学号或工号或身份证号


- Request Example

```
    http://{server-domain}/api/app/getapps
```

- Response Example

```
    {
        "name": "应用名称",
        "url": "应用链接",
        "logo": "应用logo链接"
    }
```