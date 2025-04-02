# 基于Spring AI的天气查询MCP演示项目

## 项目概述

本项目是一个基于Java 21和Spring AI框架的天气查询MCP（Model Context Protocol）演示项目。该项目展示了如何使用Spring AI全家桶与阿里云百炼MCP进行集成，实现智能天气查询和分析功能。

### 主要功能

- 获取当前天气信息
- 获取天气预报
- 使用AI生成天气分析报告
- 使用AI生成天气预报摘要

### 技术栈

- Java 21
- Spring Boot 3.2.3
- Spring AI 1.0.0-M6
- Spring WebFlux
- OpenWeatherMap API
- 阿里云百炼MCP

## 系统架构

项目采用分层架构设计，主要包含以下组件：

1. **客户端层（Client）**
   - `WeatherClient`：天气数据获取接口
   - `OpenWeatherMapClient`：OpenWeatherMap API实现
   - `AlibabaBailianMcpClient`：阿里云百炼MCP客户端

2. **服务层（Service）**
   - `WeatherService`：天气服务，整合天气数据和AI分析

3. **控制器层（Controller）**
   - `WeatherController`：提供RESTful API接口
   - `TestController`：AI组件测试接口

4. **主机层（Host）**
   - `AlibabaBailianMcpHost`：阿里云百炼MCP主机组件

5. **配置层（Config）**
   - `AlibabaBailianMcpConfig`：阿里云百炼MCP配置
   - `McpClientConfig`：MCP客户端配置

6. **模型层（Model）**
   - `WeatherResponse`：天气数据模型

## 详细设计

### 客户端层

#### WeatherClient

`WeatherClient`是一个接口，定义了获取天气数据的基本方法：

```java
public interface WeatherClient {
    Mono<WeatherResponse> getCurrentWeather(String city);
    Mono<WeatherResponse> getWeatherForecast(String city, int days);
}
```

#### OpenWeatherMapClient

`OpenWeatherMapClient`实现了`WeatherClient`接口，通过调用OpenWeatherMap API获取真实的天气数据。它使用WebClient进行HTTP请求，并将响应转换为`WeatherResponse`对象。

#### AlibabaBailianMcpClient

`AlibabaBailianMcpClient`是与阿里云百炼MCP交互的客户端组件。它提供了以下功能：

- 发送查询到AI模型
- 生成天气分析
- 生成天气预报摘要

```java
public class AlibabaBailianMcpClient {
    private final ChatClient chatClient;

    public CompletableFuture<String> query(String prompt) {
        // 实现省略
    }

    public CompletableFuture<String> generateWeatherAnalysis(String weatherData) {
        // 实现省略
    }

    public CompletableFuture<String> generateForecastSummary(String city, String forecastData) {
        // 实现省略
    }
}
```

### 服务层

#### WeatherService

`WeatherService`是核心服务组件，它整合了天气数据获取和AI分析功能：

```java
public class WeatherService {
    private final WeatherClient weatherClient;
    private final AlibabaBailianMcpClient mcpClient;

    public Mono<WeatherResponse> getCurrentWeather(String city) {
        // 实现省略
    }

    public Mono<WeatherResponse> getWeatherForecast(String city, int days) {
        // 实现省略
    }

    public Mono<String> getWeatherAnalysis(String city) {
        // 实现省略
    }

    public Mono<String> getWeatherForecastSummary(String city, int days) {
        // 实现省略
    }
}
```

### 控制器层

#### WeatherController

`WeatherController`提供了RESTful API接口，允许客户端获取天气数据和AI分析：

```java
@RestController
@RequestMapping("/api/weather")
public class WeatherController {
    private final WeatherService weatherService;

    @GetMapping("/current")
    public Mono<ResponseEntity<WeatherResponse>> getCurrentWeather(@RequestParam String city) {
        // 实现省略
    }

    @GetMapping("/forecast")
    public Mono<ResponseEntity<WeatherResponse>> getWeatherForecast(
            @RequestParam String city,
            @RequestParam(defaultValue = "5") int days) {
        // 实现省略
    }

    @GetMapping("/analysis")
    public Mono<ResponseEntity<String>> getWeatherAnalysis(@RequestParam String city) {
        // 实现省略
    }

    @GetMapping("/forecast/summary")
    public Mono<ResponseEntity<String>> getWeatherForecastSummary(
            @RequestParam String city,
            @RequestParam(defaultValue = "5") int days) {
        // 实现省略
    }
}
```

#### TestController

`TestController`提供了测试AI组件的接口：

```java
@RestController
@RequestMapping("/api/test")
public class TestController {
    private final AlibabaBailianMcpClient aiClient;
    private final AlibabaBailianMcpHost aiHost;

    @GetMapping("/client")
    public Mono<ResponseEntity<String>> testAiClient(@RequestParam String prompt) {
        // 实现省略
    }

    @GetMapping("/host")
    public Mono<ResponseEntity<String>> testAiHost(@RequestParam String prompt) {
        // 实现省略
    }
}
```

### 主机层

#### AlibabaBailianMcpHost

`AlibabaBailianMcpHost`是与阿里云百炼MCP交互的主机组件，它提供了以下功能：

```java
@Component
public class AlibabaBailianMcpHost {
    private final ChatClient chatClient;

    public Mono<String> callAiModel(String prompt) {
        // 实现省略
    }

    public Mono<String> getWeatherAnalysis(String weatherData) {
        // 实现省略
    }

    public Mono<String> getWeatherForecastSummary(String city, String forecastData) {
        // 实现省略
    }
}
```

### 配置层

#### AlibabaBailianMcpConfig

`AlibabaBailianMcpConfig`提供了阿里云百炼MCP的配置：

```java
@Configuration
public class AlibabaBailianMcpConfig {
    @Value("${alibaba.cloud.bailian.mcp.url}")
    private String mcpUrl;

    @Value("${alibaba.cloud.bailian.mcp.api-key}")
    private String mcpApiKey;

    @Bean
    public WebClient bailianMcpWebClient() {
        // 实现省略
    }
}
```

#### McpClientConfig

`McpClientConfig`提供了MCP客户端的配置：

```java
@Configuration
public class McpClientConfig {
    @Value("${spring.ai.openai.api-key}")
    private String apiKey;

    @Bean
    public ChatClient chatClient() {
        // 实现省略
    }
}
```

### 模型层

#### WeatherResponse

`WeatherResponse`是天气数据的模型类：

```java
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeatherResponse {
    private String city;
    private String country;
    private WeatherData current;
    private List<WeatherData> forecast;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WeatherData {
        private String date;
        private double temperature;
        private double feelsLike;
        private double humidity;
        private String description;
        private double windSpeed;
        private String iconUrl;
    }
}
```

## 项目配置

### application.properties

```properties
# 服务器配置
server.port=8080

# Spring AI OpenAI配置
spring.ai.openai.api-key=${OPENAI_API_KEY:your_openai_api_key}
spring.ai.openai.chat.options.model=gpt-3.5-turbo
spring.ai.openai.chat.options.temperature=0.7
spring.ai.openai.chat.options.max-tokens=1000

# OpenWeatherMap API配置
weather.api.key=${OPENWEATHER_API_KEY:your_openweathermap_api_key}
weather.api.url=https://api.openweathermap.org/data/2.5

# 阿里云百炼MCP配置
alibaba.cloud.bailian.mcp.url=${ALIBABA_MCP_URL:https://api.alibaba-inc.com/bailian/mcp}
alibaba.cloud.bailian.mcp.api-key=${ALIBABA_API_KEY:your_alibaba_cloud_bailian_mcp_api_key}
```

## 项目构建与运行

### 环境要求

- Java 21
- Maven 3.8+

### 构建项目

```bash
mvn clean package
```

### 运行项目

```bash
java -jar target/weather-mcp-demo-0.0.1-SNAPSHOT.jar
```

或者使用Maven运行：

```bash
mvn spring-boot:run
```

### 环境变量配置

运行项目前，需要设置以下环境变量：

- `OPENAI_API_KEY`：OpenAI API密钥
- `OPENWEATHER_API_KEY`：OpenWeatherMap API密钥
- `ALIBABA_API_KEY`：阿里云百炼MCP API密钥
- `ALIBABA_MCP_URL`：阿里云百炼MCP URL

## API使用示例

### 获取当前天气

```
GET /api/weather/current?city=Beijing
```

响应示例：

```json
{
  "city": "Beijing",
  "country": "China",
  "current": {
    "date": "2025-03-27",
    "temperature": 25.0,
    "feelsLike": 26.0,
    "humidity": 60.0,
    "description": "Sunny",
    "windSpeed": 5.0,
    "iconUrl": "http://openweathermap.org/img/w/01d.png"
  },
  "forecast": []
}
```

### 获取天气预报

```
GET /api/weather/forecast?city=Shanghai&days=3
```

### 获取天气分析

```
GET /api/weather/analysis?city=Guangzhou
```

响应示例：

```
广州今天天气晴朗，气温25°C，体感温度26°C，湿度60%，风速5km/h。建议穿着轻便的衣物，注意防晒。适合户外活动，但请记得补充水分。
```

### 获取天气预报摘要

```
GET /api/weather/forecast/summary?city=Shenzhen&days=5
```

响应示例：

```
深圳未来5天天气预报：周一至周三多云，气温22-28°C；周四和周五有阵雨，气温20-25°C。整体湿度较高，建议携带雨具，特别是周四和周五出行时。
```

### 测试AI客户端

```
GET /api/test/client?prompt=今天北京的天气如何？
```

### 测试AI主机

```
GET /api/test/host?prompt=分析上海明天的天气情况
```

## 测试

项目包含了全面的单元测试和集成测试，可以通过以下命令运行：

```bash
mvn test
```

## 总结

本项目展示了如何使用Spring AI全家桶与阿里云百炼MCP进行集成，实现智能天气查询和分析功能。通过使用Spring Boot和Spring WebFlux，我们构建了一个响应式的Web应用程序，能够高效地处理并发请求。

项目的核心价值在于展示了如何将传统的天气数据API与现代AI技术相结合，提供更加智能和个性化的天气信息服务。通过阿里云百炼MCP，我们能够生成自然语言的天气分析和预报摘要，使用户能够更直观地理解天气信息。

未来的改进方向包括：

1. 增加更多天气数据源
2. 优化AI提示工程，提高分析质量
3. 添加用户偏好设置，提供个性化的天气建议
4. 实现缓存机制，提高性能
5. 添加更多语言支持
