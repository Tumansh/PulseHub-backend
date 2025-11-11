<h1 align="center">🧩 PulseHub Backend — Spring Boot Microservices</h1>

<p align="center">
  A modular <b>Spring Boot Microservices Architecture</b> built for scalability, simplicity, and reusability.<br/>
  Part of the <b>PulseHub Full Stack Platform</b> integrating AI, Authentication, Expense Tracking, Weather, Diary, and Stock Insights.
</p>

<hr/>

<h2>🚀 Overview</h2>

<table>
<tr><th>Service</th><th>Description</th></tr>
<tr><td>🧠 <b>AI Service</b></td><td>Integrates with Groq/OpenAI API for intelligent chat and contextual replies.</td></tr>
<tr><td>🔐 <b>Auth Service</b></td><td>Handles user registration, login, and JWT-based authentication.</td></tr>
<tr><td>💰 <b>Expense Service</b></td><td>Tracks income, expenses, and provides savings analytics.</td></tr>
<tr><td>📔 <b>Diary Service</b></td><td>Manages user diary entries, thoughts, and notes.</td></tr>
<tr><td>🌦️ <b>Weather Service</b></td><td>Fetches live weather updates for the dashboard.</td></tr>
<tr><td>📈 <b>Stocks Service</b></td><td>Provides real-time stock data and trend analysis.</td></tr>
<tr><td>🧩 <b>Common Library</b></td><td>Contains shared utilities like <code>JwtUtil</code>, constants, and models used by all services.</td></tr>
</table>

<hr/>

<h2>🏗️ Project Structure</h2>

<pre>
backend/
├── pom.xml                     # Parent POM (aggregator)
│
├── common-lib/                 # Shared library (JwtUtil, models, constants)
│
├── ai-service/                 # AI chat microservice
├── auth-service/               # Authentication and JWT
├── expense-service/            # Income & Expense management
├── diary-service/              # Notes & diary entries
├── weather-service/            # Weather APIs
└── stocks-service/             # Stock data & analytics
</pre>

<hr/>

<h2>⚙️ Tech Stack</h2>

<ul>
  <li><b>Language:</b> Java 21</li>
  <li><b>Framework:</b> Spring Boot 3.5.x</li>
  <li><b>Database:</b> MySQL 8.x</li>
  <li><b>Build Tool:</b> Maven (Multi-Module)</li>
  <li><b>Security:</b> JWT (via <code>common-lib</code>)</li>
  <li><b>REST Client:</b> Spring RestTemplate</li>
  <li><b>AI Integration:</b> Groq / OpenAI-compatible API</li>
</ul>

<hr/>

<h2>🧰 Setup & Installation</h2>

<h3>1️⃣ Clone the Repository</h3>

<pre>
git clone https://github.com/yourusername/PulseHub.git
cd PulseHub/backend
</pre>

<h3>2️⃣ Configure Database & Secrets</h3>

Each service has an <code>application.yml</code> inside:
<pre>src/main/resources/application.yml</pre>

Example (<b>Auth Service</b>):
<pre>
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/pulsehub_auth
    username: root
    password: your_password
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
server:
  port: 8081
jwt:
  secret: your_secret_key_here
</pre>

<p><b>⚠️ Note:</b> All <code>application.yml</code> files are excluded from GitHub for security (see <code>.gitignore</code>).</p>

<h3>3️⃣ Build All Services</h3>
<pre>mvn clean install</pre>

Expected Output:
<pre>
[INFO] BUILD SUCCESS
</pre>

<h3>4️⃣ Run a Specific Service</h3>
<pre>
cd ai-service
mvn spring-boot:run
</pre>

or run the JAR:
<pre>java -jar target/ai-service-0.0.1-SNAPSHOT.jar</pre>

<hr/>

<h2>🧩 Common Library (<code>common-lib</code>)</h2>

<p>The shared library provides reusable components:</p>

<ul>
  <li><b>JwtUtil.java</b> – JWT token generation and validation</li>
  <li>Common constants and shared DTOs</li>
</ul>

Used in any service via:
<pre>
&lt;dependency&gt;
  &lt;groupId&gt;com.pulsehub.commonlib&lt;/groupId&gt;
  &lt;artifactId&gt;common-lib&lt;/artifactId&gt;
  &lt;version&gt;0.0.1-SNAPSHOT&lt;/version&gt;
&lt;/dependency&gt;
</pre>

Rebuild after editing it:
<pre>cd backend && mvn clean install</pre>

<hr/>

<h2>🧠 AI Service Configuration</h2>

<p>To use the Groq API or an OpenAI-compatible endpoint, configure in <code>application.yml</code>:</p>

<pre>
ai:
  api:
    url: https://api.groq.com/openai/v1/chat/completions
    key: YOUR_GROQ_API_KEY
</pre>

<p>If unavailable, a fallback offline AI mode provides simulated responses.</p>

<hr/>

<h2>🧱 Multi-Module Maven Setup</h2>

Parent POM defines module order:
<pre>
&lt;modules&gt;
  &lt;module&gt;common-lib&lt;/module&gt;
  &lt;module&gt;ai-service&lt;/module&gt;
  &lt;module&gt;auth-service&lt;/module&gt;
  &lt;module&gt;expense-service&lt;/module&gt;
  &lt;module&gt;diary-service&lt;/module&gt;
  &lt;module&gt;weather-service&lt;/module&gt;
  &lt;module&gt;stocks-service&lt;/module&gt;
&lt;/modules&gt;
</pre>

Each service inherits the parent:
<pre>
&lt;parent&gt;
  &lt;groupId&gt;com.pulsehub&lt;/groupId&gt;
  &lt;artifactId&gt;pulsehub-backend&lt;/artifactId&gt;
  &lt;version&gt;1.0.0&lt;/version&gt;
&lt;/parent&gt;
</pre>

<hr/>

<h2>🧾 .gitignore (Backend)</h2>

<pre>
# Build
target/
**/target/
build/
out/

# IDE
.idea/
.vscode/
*.iml

# Logs
*.log

# Environment & Secrets
.env
*.env
application.yml
application-dev.yml
application-prod.yml
application-local.yml

# OS
.DS_Store
Thumbs.db
</pre>

<hr/>

<h2>🧩 Useful Commands</h2>

<table>
<tr><th>Action</th><th>Command</th></tr>
<tr><td>Build All</td><td><code>mvn clean install</code></td></tr>
<tr><td>Skip Tests</td><td><code>mvn clean install -DskipTests</code></td></tr>
<tr><td>Run Service</td><td><code>mvn spring-boot:run</code></td></tr>
<tr><td>Clean Builds</td><td><code>mvn clean</code></td></tr>
<tr><td>Rebuild Shared Lib</td><td><code>cd backend && mvn clean install</code></td></tr>
</table>

<hr/>

<h2>🧠 Troubleshooting</h2>

<ul>
  <li><b>❌ JwtUtil not found</b> → Rebuild backend to repackage common-lib.</li>
  <li><b>❌ AI API not reachable</b> → Verify <code>ai.api.url</code> & <code>ai.api.key</code>.</li>
  <li><b>❌ Port in use</b> → Change <code>server.port</code> in <code>application.yml</code>.</li>
  <li><b>❌ DB connection failed</b> → Ensure MySQL is running with valid credentials.</li>
</ul>

<hr/>

<h2>📈 Future Enhancements</h2>

<ul>
  <li>Centralized Configuration using Spring Cloud Config</li>
  <li>Async Messaging via Kafka</li>
  <li>API Gateway with Spring Cloud Gateway</li>
  <li>OAuth2 / Keycloak Authentication</li>
  <li>Docker & Kubernetes Deployment</li>
</ul>

<hr/>

<h2>👨‍💻 Author</h2>

<p>
<b>Developer:</b> Your Name Here<br/>
<b>Tech Stack:</b> Spring Boot, Java 21, MySQL, Maven<br/>
<b>GitHub:</b> <a href="https://github.com/yourusername">github.com/yourusername</a>
</p>

<hr/>

<h2>🏁 License</h2>

<p>This project is licensed under the <b>MIT License</b> — feel free to use and adapt for your own projects.</p>
