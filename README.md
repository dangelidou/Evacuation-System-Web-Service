# Evacuation System Web Service

An Intelligent Building Evacuation System that utilizes the Dijkstra algorithm in order to suggest the shortest paths to the available exits and shelters of a given building, based on the evacuees' speed and the current conditions of each room and hallway.
This project constitutes the main result of my bachelor thesis on intelligent evacuation systems. 

## Execution Instructions
The web service is temporarily hosted [here](http://155.207.201.131:8080/dijkstra.html).

If you wish to run it at your machine, you just have to dowload the executable file [evacuation_system.jar](https://github.com/dangelidou/Evacuation-System-Web-Service/blob/main/evacuation_system.jar). Make sure you have JRE or JDK installed.
Then you can run the service by running in the cmd/bash/PowerShell the command:
```
java -jar path/to/evacuation_system.jar
```
The project will then be available at the local address: 
```
http://localhost:8080/dijkstra.html
```
To host the service on another server, you just have to move the .jar file to the server using any file transfer tool (e.g. WinSCP) or the terminal. Then you can run it using the same command stated above. The service will then be accessed through the address:
```
http://server-ip:8080/dijkstra.html
```

## Input Instructions
The service requires as input a XML file with the information regarding the building, its rooms, hallways, dimensions and conditions. The uploaded XML should follow the following format:

### Root element: `network`

#### Sub-element: `node`

| Attribute        | Description                                                        | Type     | Value Range |
|------------------|---------------------------------------------------------------------|----------|--------------|
| `id`*            | Identifier                                                          | String   |              |
| `label`          | Room description                                                    | String   |              |
| `exit`*          | If true, the room has an exit of the building                       | Boolean  |              |
| `temperature`    | Temperature sensor indication                                       | Integer  | [1, 3]       |
| `smoke`          | Smoke sensor indication                                             | Integer  | [1, 3]       |
| `co`             | Carbon monoxide sensor indication                                   | Integer  | [1, 3]       |
| `co2`            | Carbon dioxide sensor indication                                    | Integer  | [1, 3]       |
| `flood`          | If true, the room is flooded                                        | Boolean  |              |
| `water`          | Water level sensor indication in case of flooding                  | Integer  | [1, 3]       |
| `fire`           | If true, there is an ongoing fire in the room                      | Boolean  |              |
| `place`          | Location within the building                                        | String   |              |
| `ventilation`    | Ventilation status indication                                       | Integer  | [1, 3]       |
| `shelter`        | If true, the room can be used as a shelter                          | Boolean  |              |
| `people`         | Current number of people in the room                                | Integer  |              |
| `compromised`    | If true, the room is considered unsafe                              | Boolean  |              |

---

#### Sub-element: `edge`

| Attribute           | Description                                                               | Type    | Value Range |
|---------------------|---------------------------------------------------------------------------|---------|--------------|
| `id`*               | Identifier                                                                | String  |              |
| `label`             | Corridor description                                                      | String  |              |
| `from`*             | Room from which the corridor starts                                       | String  |              |
| `to`*               | Room to which the corridor leads                                          | String  |              |
| `lengths`*          | Length of the corridor                                                    | Float   |              |
| `disability`        | If true, the corridor is suitable for people with mobility difficulties   | Boolean |              |
| `speed`*            | Average traversal speed                                                   | Float   |              |
| `disability_speed`  | Average traversal speed for people with mobility difficulties             | Float   |              |
| `widths`*           | Corridor width                                                            | Float   |              |
| `temperature`       | Temperature sensor indication                                             | Integer | [1, 3]       |
| `smoke`             | Smoke sensor indication                                                   | Integer | [1, 3]       |
| `co`                | Carbon monoxide sensor indication                                         | Integer | [1, 3]       |
| `co2`               | Carbon dioxide sensor indication                                          | Integer | [1, 3]       |
| `flood`             | If true, the corridor is flooded                                          | Boolean |              |
| `fire`              | If true, there is an ongoing fire in the corridor                         | Boolean |              |
| `crowd`             | Current number of people in the corridor                                  | Integer |              |
| `compromised`       | If true, the corridor is considered unsafe                                | Boolean |              |

---

\* Fields marked with an asterisk are mandatory.

A sample building XML description is included in the resources folder for reference ([modified_2nd_floor_v2.xml](https://github.com/dangelidou/Evacuation-System-Web-Service/blob/main/src/main/resources/modified_2nd_floor_v2.xml))

## How to get the shortest paths to a building's exits/shelters from every room:
1. Click at the 'Choose file' button
2. Browse and select the XML file corresponding to the building of interest
3. Click at the 'Upload and Get Shortest Paths' button
4. The results will show in two tables: the first one refers to evacuees with full mobility, while the second one includes only accessible routes for individuals with disabilities.

\* If the XML doesn't follow the right structure, a corresponding error message will show.
