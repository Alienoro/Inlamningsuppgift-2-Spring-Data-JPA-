#Välkommen till Bilverkstaden!
Detta är ett REST-baserat projekt byggt med Spring Boot, Spring Data JPA och H2 för att hantera kunder och fordon.

#Instruktioner
För att starta projektet så kan du börja med att klona repositoryt och sedan gå till projektmappen. 
Sedan kan du skriva ./mvnw spring-boot:run
Nästa steg är att öppna en ny terminal och navigera till projektmappen igen.

#Endpoints
För att köra de olika endpointsen så gör såhär...

Skapa en kund.
curl -X POST "http://localhost:8080/customers?name=Anna&phone=123456789"

Hämta kund-id via ett namn.
curl -X GET "http://localhost:8080/customer-id?name=Anna"

Lista alla kunder med fordon.
curl -X GET "http://localhost:8080/customers"

Skapa ett fordon.

curl -X POST "http://localhost:8080/vehicles?registrationNumber=ABC123&brand=Volvo&model=V60&productionYear=2022"

Lista alla fordon. 
curl -X GET "http://localhost:8080/vehicles"

Lista fordon efter bilmärke.
curl -X GET "http://localhost:8080/vehicles-by-brand?brand=Volvo"

Koppla ett fordon till en kund.
curl -X POST "http://localhost:8080/addvehicletocustomer?customerId=1&vehicleId=1"

Uppdatera bilmärket för ett fordon
curl -X PUT "http://localhost:8080/vehicles/1/brand?brand=Saab"

Lista registreringsnummer och bilmärke
curl -X GET "http://localhost:8080/vehicle-brand-info"
