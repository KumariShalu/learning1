package service.facade;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.inject.Inject;
import domain.Child;

@Stateless
@Named("child")
public class ChildFacade extends AbstractFacade<Child, Long> {

    @Inject
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ChildFacade() {
        super(Child.class);
    }

}
