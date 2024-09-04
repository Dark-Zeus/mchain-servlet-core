package me.sunera.mchain.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 
 * 
 * <pre>
 * class MyController extends Middleware {
 *   {@code @Override}
 *   public void run(HttpServletRequest request, HttpServletResponse response, MChain next) {
 *       // Your controller logic here
 *      next.run();
 *   }
 *}
 * </pre>
 * 
 * @see MChain
 * @see MChain#run(HttpServletRequest, HttpServletResponse, MChain)
 * @see jakarta.servlet.http.HttpServletRequest
 * @see jakarta.servlet.http.HttpServletResponse
 * 
 */
public abstract class Middleware extends MChain {
    private Middleware(HttpServletRequest request, HttpServletResponse response){
        super(request, response);
    }

    public Middleware(){
        super(null, null);
    }

    @Override
    protected abstract void run(HttpServletRequest request, HttpServletResponse response, MChain next);

    @Override
    protected final void run(HttpServletRequest request, HttpServletResponse response) {

    }
}