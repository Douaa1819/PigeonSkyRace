# 🕊️ Pigeon Competition Management

## 🎯 Project Context
This project manages competitions organized by the **Moroccan Federation of Racing Pigeons**, involving multiple breeders participating in three race types: **speed**, **mid-distance**, and **long-distance**.  
The goal is to ensure competition integrity through an application that enforces strict rules and optimizes event management.

---

## 🚀 Key Features

### For Breeders 🦜
- **Registration**:
  - Create an account with:
    - Unique loft name
    - Username & password
    - GPS coordinates of the loft
- **Pigeon Management**:
  - Add pigeons with:
    - Unique ring number
    - Gender, age, color, and image

### For Organizers 🧑‍🏫
- **Competition Management**:
  - Define competitions with:
    - Name, GPS coordinates of the release point
    - Start date/time and expected distance
  - Add participating pigeons via ring numbers
  - Close competitions and trigger result calculation
- **Data Upload**:
  - Arrival times and pigeon ring numbers
- **Results Viewing & Export**:
  - Generate PDF reports of competition results

---

## 📊 Result Calculation
1. **Data Collection**: Record pigeon arrival times and ring numbers  
2. **Distance Calculation**: Using the **Haversine formula** to compute distances between GPS points  
3. **Flight Time**: Difference between release time and arrival time  
4. **Speed Calculation**:  
   - Speed = Distance / Flight Time  
   - Adjusted by a coefficient based on average distance of pigeons  
5. **Ranking**: Pigeons ranked by speed, fastest to slowest  
6. **Point Allocation**:  
   - Points based on ranking and admission percentage  
   - Total points of top 5 pigeons per loft for overall leaderboard

---

## 📈 Results Display
- Results per race include:
  - **Loft**, **Ring Number**, **Arrival Time**, **Distance**, **Speed**, **Points**
- Automatic general ranking for viewing/export

---

## ⚙️ Technical Requirements
- **Backend**: Spring Boot REST API  
- **Database**: MongoDB  
- **Architecture**: Layered application (Controller → Service → Repository)  
- **Data Validation**: Ensures integrity of information  
- **Exception Handling**: Centralized for consistent error management  
- **Unit Testing**: Ensures code reliability  
- **Configuration**: YAML-based configuration files  

---

## 🐳 Docker Installation (Optional)
Use Docker to run MongoDB and Mongo Express:

1. Launch Docker Compose:  
```bash
docker-compose up -d
```

## 🏗️ Project Architecture

### Backend

Built with Spring Boot, providing RESTful services and web endpoints.

### Database

MongoDB, a flexible NoSQL database, storing all competition, pigeon, and breeder data.

### Data Model

Data is transferred via DTOs (Data Transfer Objects) to structure information between application layers.

### MapStruct

Used for automatic mapping between DTOs and entities, improving code readability and reducing boilerplate.
### Lombok

**Lombok** Generates getters, setters, constructors, and other utility methods automatically.

### Docker

The project uses **Docker** to facilitate the execution of MongoDB and Mongo Express, a web interface that makes it easy to interact with MongoDB.

## Prerequisites 🛠️

Avant de démarrer l'application, assurez-vous d'avoir les éléments suivants installés :

- **Java 11 +** : [Download JDK](https://adoptopenjdk.net/).
- **Maven** : [Download Maven](https://maven.apache.org/).
- **Docker** : [for MongoDB and Mongo Express](https://www.docker.com/products/docker-desktop).

## Running the Application🏃‍♂️

### 1. Clone the repository:

```bash
git clone https://github.com/erradaoumaimaa/PigeonSkyRace.git
```
## 2. Navigate to the project directory:

```bash
cd PigeonSkyRace
```
## 3. Build the project with Maven:

```bash
mvn clean install
```

## 4. Run the Spring Boot application:

```bash
mvn spring-boot:run
```

## Quick Local Login (Seeded Accounts)

The backend auto-seeds demo users at startup via `DemoAccountsSeeder`, so you can immediately test:

- Admin
  - Email: `admin@pigeonskyrace.ma`
  - Password: `admin123`
- Organizer
  - Email: `organizer@pigeonskyrace.ma`
  - Password: `organizer123`
- Breeder
  - Email: `breeder@test.ma`
  - Password: `breeder123`

Use these for full flow checks:

1. Register (optional, new breeder account)
2. Login with seeded account
3. Verify role-based dashboard routing

## Environment Variables

Create a local `.env` from `.env.example` (or export directly in your shell) before running Docker + backend.

