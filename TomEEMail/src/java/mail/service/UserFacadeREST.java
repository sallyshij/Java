/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mail.service;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import mail.User;

/**
 *
 * @author macpro
 */
@Stateless
@Path("user")
public class UserFacadeREST extends AbstractFacade<User> {

    @PersistenceContext(unitName = "TomEEMailPU")
    private EntityManager em;

    public UserFacadeREST() {
        super(User.class);
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public Integer create(Integer id, User entity) {
        super.create(entity);
        em.flush();
        return entity.getIduser(); 
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, User entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String findKey(@QueryParam("user") String user, @QueryParam("password") String pwd) {
        Integer p = confirmPwd(pwd);
        User u = null;
        User u1= null;
        try {
            u = em.createNamedQuery("User.findByName",User.class).setParameter("name",user).getSingleResult();
            u1 = em.createNamedQuery("User.findByPassword",User.class).setParameter("password",pwd).getSingleResult();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        if(p != null){
            if(u1 != null && u == u1){
                return Integer.toString(u1.getIduser());}
        }
        return "";
    }
    
    private Integer confirmPwd(String pwd) {
        User u = null;
        try {
            u = em.createNamedQuery("User.findByPassword",User.class).setParameter("password",pwd).getSingleResult();
        } catch(Exception ex) {}
        if(u != null)
            return u.getIduser();
        return null;
    }
    
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public String find(@QueryParam("user") String name) {
        User u = null;
        try {
            u = em.createNamedQuery("User.findByName",User.class).setParameter("name",name).getSingleResult();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        if(u != null)
            return Integer.toString(u.getIduser());
        return "";
    }

    
    /*@GET
    @Override
    @Produces({MediaType.APPLICATION_JSON})
    public List<User> findAll() {
        return super.findAll();
    }*/

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<User> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
