package hr.fer.zemris.java.tecaj_13.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;

import hr.fer.zemris.java.tecaj_13.dao.DAO;
import hr.fer.zemris.java.tecaj_13.dao.DAOException;
import hr.fer.zemris.java.tecaj_13.model.BlogEntry;
import hr.fer.zemris.java.tecaj_13.model.BlogMessage;
import hr.fer.zemris.java.tecaj_13.model.BlogUser;

public class JPADAOImpl implements DAO {

	@Override
	public BlogEntry getBlogEntry(Long id) throws DAOException {
		BlogEntry blogEntry = JPAEMProvider.getEntityManager().find(BlogEntry.class, id);
		return blogEntry;
	}

	
	@Override
	public List<BlogUser> getBlogUsers() throws DAOException {
		
		@SuppressWarnings("unchecked")
		List<BlogUser> blogers = 
				(List<BlogUser>) JPAEMProvider.getEntityManager()
											 .createQuery("select user from BlogUser as user")
											 .getResultList();
		return blogers;
	}


	@Override
	public BlogUser getBlogUserByNick(String nick) throws DAOException {
		
		@SuppressWarnings("unchecked")
		List<BlogUser> bloger = (List<BlogUser>) JPAEMProvider.getEntityManager()
									   .createQuery("select user from BlogUser as user where user.nick=:nick")
									   .setParameter("nick", nick)
									   .getResultList();
		
		return bloger.size() == 0 ? null : bloger.get(0);
	}


	@Override
	public boolean storeBlogUser(BlogUser bloger) throws DAOException {
		
		JPAEMProvider.getEntityManager().persist(bloger);
		
		return true;
	}


	@Override
	public boolean storeMessage(String message, Long userId) throws DAOException {
		EntityManager em = JPAEMProvider.getEntityManager();
		
		BlogUser user = em.find(BlogUser.class, userId);
		
		BlogMessage blogMessage = new BlogMessage();
		
		blogMessage.setContent(message);
		
		em.persist(blogMessage);
		
		user.getMessages().add(blogMessage);
		
		return true;
	}

}