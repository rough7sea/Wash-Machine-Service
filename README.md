Data Base [properties](src/main/resources/application.properties)
Test Data Base [properties](src/test/resources/application-test.properties)

Staring port 8080

Before migration must be washmachinedb and washmachine_test_db instants. 
Migration will be applied automatically to DB.

General idea in HTTP connection between WashMachine <-> Server and User <-> Server
to get require information or put Machine ow Wash state in them.

Sawagger UI: http://localhost:8080/swagger-ui.html

Some curl examples:

Create machine or update: 
curl -L -X POST "http://localhost:8080/wash/machine/" -H "Content-Type: application/json" --data-raw "{\"name\": \"My machine\",\"status\": \"ACTIVE\"}"

Get from response machineId and check machine status:
curl -L -X GET "http://localhost:8080/wash/machine/{machineId}" -H "Content-Type: application/json"

Machine can be updated with {machineId} in body:
curl -L -X POST "http://localhost:8080/wash/machine/" -H "Content-Type: application/json" --data-raw "{ \"id\":\"{machineId}\", \"name\": \"My machine 1\", \"status\": \"ACTIVE\" }"

Feel free to delete machine:
curl -L -X DELETE "http://localhost:8080/wash/machine/{machineId}" -H "Content-Type: application/json" --data-raw "{ \"name\": \"My machine\", \"status\": \"ACTIVE\" }"

Or get all machines:
curl -L -X GET "http://localhost:8080/wash/machine/" -H "Content-Type: application/json"


If we know machine id we can create action(washing) on them. Actions also have events(steps).


Machine : 
- Action 1
    - Start
    - Wash 
    - Rinse
    - Complete
- Action 2
    - Start 
    - Wash
    - ...
    - Complete


Action have there [WashStep](src/main/java/com/example/washmachine/common/WashStep.java) 
and can be in different [WashActionStatus](src/main/java/com/example/washmachine/common/WashActionStatus.java)

Also Action(Washing) having there [WashMode](src/main/java/com/example/washmachine/common/WashMode.java) 
and [WashParams](src/main/java/com/example/washmachine/entity/WashParams.java)

Using {machineId} we can create action without custom params: 
curl -L -X POST "http://localhost:8080/wash/action/" -H "Content-Type: application/json" --data-raw "{ \"machineId\":\"{machineId}\", \"washMode\":\"DAILY\", \"status\":\"PROCESS\" }"

or with them:
curl -L -X POST "http://localhost:8080/wash/action/" -H "Content-Type: application/json" --data-raw "{ \"machineId\":\"{machineId}\", \"washMode\":\"DAILY\", \"status\":\"PROCESS\", \"customParams\":{ \"spinPower\":800, \"rinsesCount\":4, \"temperature\":45, \"powder\":\"My special powder\", \"conditioner\":\"My special conditioner\" } }"

To check current action by {actionID}:
curl -L -X GET "http://localhost:8080/wash/action/{actionID}" 

Or you can get action by {machineId}:
curl -L -X GET "http://localhost:8080/wash/action/machine/{machineId}"


After creating action machine can show their status by sending events.

For event creating need {actionId} in request body:
curl -L -X POST "http://localhost:8080/wash/event/" -H "Content-Type: application/json" --data-raw "{ \"actionId\":\"{actionId}\", \"step\":\"START\" }"

For checking current event by User with {actionId}:
curl -L -X GET "http://localhost:8080/wash/event/current/{actionId}" 

Or User can check all events in action with {actionId}:
curl -L -X GET "http://localhost:8080/wash/event/action/{actionId}"