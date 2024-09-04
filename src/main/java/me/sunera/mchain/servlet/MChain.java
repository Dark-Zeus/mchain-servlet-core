package me.sunera.mchain.servlet;

import java.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Middleware chain class to handle middleware logic in a chain
 * 
 * @version 1.0-BETA0
 * @since 2024-09-04
 * @see IOException
 * @see MChain
 * @see MChain#use(MChain...)
 * @see MChain#run(HttpServletRequest, HttpServletResponse, MChain)
 * @see MChain#run(HttpServletRequest, HttpServletResponse)
 * @see jakarta.servlet.http.HttpServletRequest
 * @see jakarta.servlet.http.HttpServletResponse
 */
public class MChain {

    private HttpServletRequest request; // Request object
    private HttpServletResponse response; // Response object

    private MChain next; // Next middleware in the chain

    /**
     * <pre>
     * MChain chain = new MChain(request, response);
     * </pre>
     * 
     * Constructor with request and response objects
     * 
     * @param request  HttpServletRequest object
     * @param response HttpServletResponse object
     * @see jakarta.servlet.http.HttpServletRequest
     * @see jakarta.servlet.http.HttpServletResponse
     * 
     */
    public MChain(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    // Disable default constructor
    private MChain(){}

    /**
     * <pre>
     * MChain chain = new MChain(request, response);
     *chain.use(middleware1, middleware2, middleware3, ..., controller);
     * </pre>
     * 
     * Method to start the middleware chain
     * 
     * @param middlewares MChain objects (Middleware / Controller)
     * 
     * @see MChain
     * @see MChain#run(HttpServletRequest, HttpServletResponse, MChain)
     * @see MChain#run(HttpServletRequest, HttpServletResponse)
     */
    public final void use(MChain... middlewares) {

        // Check if middlewares are provided
        if (middlewares.length > 0) {

            this.next = middlewares[0]; // Set the first middleware
            MChain current = this.next; // Set the current middleware

            for (int i = 1; i < middlewares.length; i++) {
                current.next = middlewares[i]; // Set the next middleware
                current = middlewares[i]; // Set the new current middleware
            }

            run(); // Start the middleware chain
        } else {
            System.out.println("No middlewares provided.");
        }
    }

    /**
     *<pre>
     * class Middleware extends MChain {
     *  {@code @Override}
     *  protected void run(HttpServletRequest req, HttpServletResponse res, MChain next){
     *      // Middleware logic
     *      next.run();
     *  }
     *}
     *</pre>
     * 
     * Method to start the next middleware in the chain
     * 
     * @see MChain
     * @see MChain#run(HttpServletRequest, HttpServletResponse, MChain)
     */
    public final void run() {
        try {
            // Check if the next middleware is available
            if (this.next != null && this.next.next != null) {
                this.next.request = this.request; // Set the request object of the next middleware
                this.next.response = this.response; // Set the response object of the next middleware
                this.next.run(this.request, this.response, this.next); // Start the next middleware
            } else {
                this.next.request = this.request;
                this.next.response = this.response;
                this.next.run(request, response);
            }
        } catch (Exception e) {
            handleError(e, this.response);
        }
    }

    /**
     * <pre>
     *class Middleware extends MChain {
     *  {@code @Override}
     *  protected void run(HttpServletRequest req, HttpServletResponse res, MChain next){
     *     // Middleware logic
     *    next.run();
     *  }
     *}
     * </pre>
     * 
     * Method to handle middleware logic
     * 
     * @param request  HttpServletRequest object
     * @param response HttpServletResponse object
     * @param next     Next middleware in the chain
     * 
     * @see MChain
     * @see jakarta.servlet.http.HttpServletRequest
     * @see jakarta.servlet.http.HttpServletResponse
    */
    protected void run(HttpServletRequest request, HttpServletResponse response, MChain next) {

    }

    /**
     * <pre>
     * class Controller extends MChain {
     *  {@code @Override}
     *  protected void run(HttpServletRequest req, HttpServletResponse res){
     *     // Controller logic
     *  }
     *}
     * </pre>
     * 
     * Method to handle controller logic
     * 
     * @param request  HttpServletRequest object
     * @param response HttpServletResponse object
     * 
     * @see jakarta.servlet.http.HttpServletRequest
     * @see jakarta.servlet.http.HttpServletResponse
     */
    protected void run(HttpServletRequest request, HttpServletResponse response) {

    }

    // Method to handle errors
    private void handleError(Exception e, HttpServletResponse response) {
        // Log the exception
        e.printStackTrace();

        // Set error response
        try {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print("{\"error\": \"An error occurred.\"}");
        } catch (IOException ioException) {
            ioException.printStackTrace(); // Handle IO exception during error response
        }
    }
}
