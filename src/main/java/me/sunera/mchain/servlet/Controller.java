package me.sunera.mchain.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Controller class should be at the end of Each middleware chain.
 * 
 * <pre>
 * class MyController extends Controller {
 *   {@code @Override}
 *   public void run(HttpServletRequest request, HttpServletResponse response) {
 *       // Your controller logic here
 *   }
 *}
 * </pre>
 * 
 * @see MChain
 * @see MChain#run(HttpServletRequest, HttpServletResponse)
 * @see jakarta.servlet.http.HttpServletRequest
 * @see jakarta.servlet.http.HttpServletResponse
 * 
 */
public abstract class Controller extends MChain {
    private Controller(HttpServletRequest request, HttpServletResponse response){
        super(request, response);
    }

    public Controller(){
        super(null, null);
    }

    protected final void run(HttpServletRequest request, HttpServletResponse response, MChain next) {
        run(request, response);
    }

    @Override
    protected abstract void run(HttpServletRequest request, HttpServletResponse response);
    
}
