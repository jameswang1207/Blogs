https://docs.mongodb.com/manual/reference/operator/aggregation/count/

db.FACE_IMAGE_TYPE.aggregate([
   {
     $match:{code:{$in:[100,101,200,201]}}
   },
   { $sort : { createdTime : 1}},
   {
     $group : { _id : "$parentId", FACE_IMAGE_TYPE: { $push: "$$ROOT" } }
   },
   { $limit : 2 },
   { $skip : 0 }
 ])
db.FACE_IMAGE_TYPE.aggregate([
   {
     $match:{code:{$in:[100,101,200,201]}}
   },
   { $sort : { createdTime : 1}},
   {
     $group : { _id : "$parentId", FACE_IMAGE_TYPE: { $push: "$$ROOT" } }
   },
   { $count : "test" }
])