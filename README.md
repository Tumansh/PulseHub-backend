<h1 align="center">🧩 PulseHub Backend — Spring Boot Multi-Module Application</h1>

<p align="center">
  Modular backend application built using <b>Spring Boot</b> and a <b>Maven multi-module</b> structure.<br/>
  Designed to support authentication, business APIs, and external integrations in a clean and maintainable way.
</p>

<hr/>

<h2>🚀 Overview</h2>

<p>
PulseHub Backend powers the server-side of the PulseHub platform.
It exposes REST APIs for authentication, expense tracking, diary management,
and external data integrations such as weather, stocks, and AI-based chat.
</p>

<p>
The application follows a <b>layered architecture</b> and uses
<b>JWT-based stateless authentication</b> implemented using Spring Security.
</p>

<hr/>

<h2>🏗️ Project Structure</h2>

<pre>
backend/
├── pom.xml                 # Parent Maven POM (aggregator)
│
├── ai-service/             # AI chat and external AI API integration
├── auth-service/           # User registration, login, and JWT authentication
├── expense-service/        # Expense and income management APIs
├── diary-service/          # Personal diary and notes APIs
├── weather-service/        # Weather data integration
└── stocks-service/         # Stock market data integration
</pre>

<p>
Each service is maintained as a separate Maven module to keep responsibilities
clearly separated and to simplify development and debugging.
</p>

<hr/>

<h2>🔐 Authentication & Security</h2>

<ul>
  <li>JWT-based stateless authentication</li>
  <li>Authentication handled by <b>auth-service</b></li>
  <li>JWT tokens issued on successful login</li>
  <li>Protected APIs validate JWT on each request</li>
  <li>Spring Security used for request filtering and access control</li>
</ul>

<p>
JWT utility and security configuration are implemented within the authentication service.
Other services rely on token validation via Spring Security configuration.
</p>

<hr/>

<h2>🧠 Architecture</h2>

<pre>
Controller → Service → Repository
</pre>

<ul>
  <li><b>Controllers</b> handle HTTP requests and responses</li>
  <li><b>Services</b> contain business logic</li>
  <li><b>Repositories</b> interact with the database using JPA</li>
</ul>

<hr/>

<h2>⚙️ Tech Stack</h2>

<table>
<tr><th>Layer</th><th>Technology</th></tr>
<tr><td>Language</td><td>Java 21</td></tr>
<tr><td>Framework</td><td>Spring Boot 3.x</td></tr>
<tr><td>Build Tool</td><td>Maven (Multi-Module)</td></tr>
<tr><td>Database</td><td>MySQL</td></tr>
<tr><td>Security</td><td>Spring Security + JWT</td></tr>
<tr><td>API Style</td><td>REST</td></tr>
</table>

<hr/>

<h2>🧰 Setup & Run</h2>

<h3>1️⃣ Clone the Repository</h3>

<pre>
git clone https://github.com/Tumansh/pulsehub-backend.git
cd pulsehub-backend
</pre>

<h3>2️⃣ Configure Database & Secrets</h3>

<p>
Each service contains an <code>application.yml</code> file inside:
</p>

<pre>
src/main/resources/application.yml
</pre>

<p>Example configuration:</p>

<pre>
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/pulsehub
    username: root
    password: your_password
jwt:
  secret: your_secret_key
</pre>

<p>
<b>Note:</b> Configuration files are excluded from GitHub using <code>.gitignore</code>.
</p>

<hr/>

<h3>3️⃣ Build All Services</h3>

<pre>
mvn clean install
</pre>

<h3>4️⃣ Run a Specific Service</h3>

<pre>
cd auth-service
mvn spring-boot:run
</pre>

<p>
Other services can be started in the same way.
</p>

<hr/>

<h2>📈 Future Enhancements</h2>

<ul>
  <li>Centralized API gateway</li>
  <li>Improved inter-service communication</li>
  <li>Docker-based deployment</li>
</ul>

<hr/>

<h2>📄 License</h2>

<p>
This project is licensed under the <b>MIT License</b>.
</p>
