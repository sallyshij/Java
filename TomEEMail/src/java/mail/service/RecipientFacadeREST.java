/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mail.service;

import java.util.ArrayList;
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
import javax.ws.rs.core.Response;
import mail.Message;
import mail.Recipient;
import mail.User;

/**
 *
 * @author macpro
 */
@Stateless
@Path("recipient")
public class RecipientFacadeREST extends AbstractFacade<Recipient> {

    @PersistenceContext(unitName = "TomEEMailPU")
    private EntityManager em;

    public RecipientFacadeREST() {
        super(Recipient.class);
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public Integer create(Integer id, Recipient entity) {
        super.create(entity);
        em.flush();
        return entity.getIdrecipient();
    } 

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Recipient entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        Recipient r = null;
        try {
            r = em.createNamedQuery("Recipient.findByIdrecipient", Recipient.class).setParameter("idrecipient",id).getSingleResult();
        } catch(Exception ex) {}
        int i = r.getMessage();
        super.remove(super.find(id));
        
        List<Recipient> results = new ArrayList<Recipient>();
        try {
            results = em.createNamedQuery("Recipient.findByMessage", Recipient.class).setParameter("message",i).getResultList();
        } catch(Exception ex) {}

        if(results.isEmpty()){
            Message m;
            m = em.createNamedQuery("Message.findByIdmessage", Message.class).setParameter("idmessage",i).getSingleResult();
            em.remove(m);
            em.flush();
        }
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Recipient find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_JSON})
    public List<Recipient> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Recipient> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
