# Dental application using REST (Spring Boot + REST)
Dental appointment application using Spring Boot + REST

Data is saved in memory in Maps. Can be easily modified to accomodate DB.

$ git clone https://github.com/sivakumarkumaravelu/dentalapplication.git

$ cd dentalapplication

$ mvn spring-boot:run

# POST Requests

Open PostMan send the response body for POST request, choose Raw -> Application/JSON

Sample request:
{
  "startTime": 1564847524000,
  "endTime": 1564851124000,
  "dentist_id": 1,
  "patient_id": 2
}

http://localhost:8080/dentalAppointments

# Get Requests

In the POST request as a part of header we get the id that is generated and sent use the same in the URL

http://localhost:8080/dentalAppointments/1
