# MChain Middleware Framework

**MChain** is a Java framework for managing middleware chains in Jakarta Servlets. It allows you to create a series of middleware components that process HTTP requests and responses in a chain, making it easy to handle common tasks such as authentication, logging, and response formatting.

## License

This project is licensed under the [GNU General Public License (GPL) v3.0](https://www.gnu.org/licenses/gpl-3.0.html). See the [LICENSE](LICENSE) file for details.

## Requirements

- **Java**: 22 (should be compatible with other versions - not tested)
- **Jakarta Servlets**: 6.0 (should be compatible with other versions - not tested)

## Getting Started

To use the MChain framework, you'll need a Jakarta Servlet container (such as Apache Tomcat) and a Java development environment.

You can either build the framework from source or download a pre-built JAR file. After that, you can import the MChain package into your project.

```java
import me.sunera.mchain.servlet;
```

### 1. Build it Yourself

1. **Clone the Repository:**

   ```bash
   git clone https://github.com/Dark-Zeus/mchain-servlet-core
   cd your-repository
   ```

2. **Build the Project:**

   Make sure you have Maven installed. Run the following command to build the project:

   ```bash
   mvn clean install
   ```

   This will compile the code and package it into a JAR file.

### 2. Using MChain Standalone JAR

To use MChain as a standalone JAR, follow these steps:

1. **Download the JAR File**

    Download the latest release file from the releases page.

2. **Include the JAR in Your Project**

    * For a Java Application: Add the JAR file to your project’s classpath. This can typically be done by placing the JAR file in a libs directory and configuring your build tool to include it.

    * For a Servlet-based Application: If you are using an IDE like Eclipse or IntelliJ IDEA, you can add the JAR to your project by:

        * Eclipse: Right-click your project > Properties > Java Build Path > Libraries > Add JARs or Add External JARs.
        * IntelliJ IDEA: Right-click your project > Open Module Settings > Libraries > + > Java and select the JAR file.

### Usage

1. **Create Middleware Classes:**

   Extend the `Middleware` class to create your middleware components. Each middleware should implement the `run(HttpServletRequest request, HttpServletResponse response, MChain next)` method.

   Middleware can be implemented in two ways: using default parameters or with additional inputs.

   1. **Default :**
      In the default approach, the middleware operates with parameters provided in the HttpServletRequest object. Here’s an example:

        ```java
        public class ExampleMiddleware1 extends Middleware {
            @Override
            public void run(HttpServletRequest request, HttpServletResponse response, MChain next) {
                String auth = request.getAttriute("auth");
                try{
                    PrintWriter out = response.getWriter();
                    if(!request.getAttribute("auth").toString().isBlank()){
                        next.run();
                    }else{
                        out.print("Empty Authorization");
                    }
                }catch(Exception e){

                }
            }
        ```

        In this example, `ExampleMiddleware1` checks for the presence of an `"auth"` attribute in the request. If the attribute is not blank, it proceeds to the next middleware; otherwise, it responds with an "Empty Authorization" message.

   2. **Middleware with Additional Inputs :**
    In this approach, middleware requires additional inputs, which are provided through the constructor. Here’s an example:

        ```Java
        public class ExampleMiddleware2 extends Middleware {

            String userType;

            public ExampleMiddleware2(String userType){
                this.userType = userType;
            }

            @Override
            public void run(HttpServletRequest request, HttpServletResponse response, MChain next) {
                    String user = request.getAttriute("user");
                    try{
                        PrintWriter out = response.getWriter();
                        if(request.getAttribute("auth").toString().equalsIgnoreCase(userType)){
                            next.run();
                        }else{
                            out.print("Wrong User Type");
                        }
                    }catch(Exception e){

                    }
            }
        }
        ```

        In this example, `ExampleMiddleware2` uses an additional userType parameter provided via its constructor. It checks if the `"user"` attribute in the request matches the expected userType. If so, it proceeds to the next middleware; otherwise, it responds with a "Wrong User Type" message.


2. **Define a Controller:**

    Extend the Controller class to handle the final logic after the middleware chain. (Similarly to Middleware additional parameters can be passed through the constructor)

    ```java
    public class ExampleController extends Controller {
        @Override
        public void run(HttpServletRequest request, HttpServletResponse response) {
            System.out.println("Final Method");
            try {
                if(!request.getAttribute("auth").toString().isBlank()){
                    out.print("Authorized");
                }else{
                    out.print("Empty Authorization");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    ```

3. **Using Defined Middlewares and Controllers in a Servlet**

    Use the `MChain` Class within a servlet to apply your middleware and controller.

    ```java
    @WebServlet("/api")
    public class ExampleServlet extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            MChain app = new MChain(request, response);

            app.use(
                new ExampleMiddleware1(),
                new ExampleMiddleware2("admin"),
                new ExampleController()
            );
        }
    }
    ```

## Contributing
Feel free to contribute to this project by submitting issues, pull requests, or suggestions. Please ensure that any contributions are compatible with the GPL license.

## Acknowledgements
* `Jakarta Servlets` for providing a powerful API for handling HTTP requests and responses.
* `Maven` for project management and build automation.