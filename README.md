# README.md

## How to run

1. Update DB config with correct URL, username and password
2. Then run `mvn clean package` to build the application
3. Run the application by `java -jar target/takehome-root-0.0.1-SNAPSHOT.jar`
4. App will run at `http://localhost:8848`

## Access endpoint from `curl` command
Run command
`curl -i -X POST -H 'Content-Type: application/json' -d '{"medallions": ["B672154F0FD3D6B5277580C3B7CBBF8E", "06D29AF4D2C6C5A9E08FAC8F7FB425F5"], "pickupDate": "2013-12-30"}' 'http://localhost:8848/trip/search'`

## Reset Cache
You can reset cache by using 
`curl -i -X GET 'http://localhost:8848/caches/reset'`

## Places to improve 
1. Add some indexes on DB for better search performance
2. Better error handling and input validation
3. Better test coverage