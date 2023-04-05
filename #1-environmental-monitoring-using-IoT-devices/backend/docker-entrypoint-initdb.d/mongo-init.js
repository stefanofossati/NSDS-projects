print('Start #################################################################');


db = db.getSiblingDB('test-db');
db.createUser(
  {
    user: "noderedTest",
    pwd: "test",
    roles: [ { role: "readWrite", db: "test-db" } ]
  }
);

db.createCollection('temp');
db.createCollection('humidity');

print('END #################################################################');