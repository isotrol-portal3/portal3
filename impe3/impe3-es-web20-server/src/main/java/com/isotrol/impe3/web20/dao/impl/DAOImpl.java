/**
 * This file is part of Port@l
 * Port@l 3.0 - Portal Engine and Management System
 * Copyright (C) 2010  Isotrol, SA.  http://www.isotrol.com
 *
 * Port@l is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Port@l is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Port@l.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.isotrol.impe3.web20.dao.impl;


import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.hibernate.Criteria;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.isotrol.impe3.dto.OrderDTO;
import com.isotrol.impe3.dto.PageDTO;
import com.isotrol.impe3.dto.PageFilter;
import com.isotrol.impe3.dto.PaginationDTO;
import com.isotrol.impe3.dto.ServiceException;
import com.isotrol.impe3.hib.query.FilterSupport;
import com.isotrol.impe3.hib.query.PageSupport;
import com.isotrol.impe3.web20.api.CommunityFilterDTO;
import com.isotrol.impe3.web20.api.CommunityMembersFilterDTO;
import com.isotrol.impe3.web20.api.FavoriteFilterDTO;
import com.isotrol.impe3.web20.api.MemberFilterDTO;
import com.isotrol.impe3.web20.api.MembershipSelFilterDTO;
import com.isotrol.impe3.web20.api.NoticeFilterDTO;
import com.isotrol.impe3.web20.api.RecommendationFilterDTO;
import com.isotrol.impe3.web20.api.SequenceNotFoundException;
import com.isotrol.impe3.web20.dao.DAO;
import com.isotrol.impe3.web20.model.AggregationEntity;
import com.isotrol.impe3.web20.model.CommentEntity;
import com.isotrol.impe3.web20.model.CommentRateEntity;
import com.isotrol.impe3.web20.model.CommunityEntity;
import com.isotrol.impe3.web20.model.CounterDailyBreakdown;
import com.isotrol.impe3.web20.model.CounterEntity;
import com.isotrol.impe3.web20.model.CounterEventEntity;
import com.isotrol.impe3.web20.model.CounterHourlyBreakdown;
import com.isotrol.impe3.web20.model.CounterMonthlyBreakdown;
import com.isotrol.impe3.web20.model.CounterPK;
import com.isotrol.impe3.web20.model.CounterTypeEntity;
import com.isotrol.impe3.web20.model.CounterYearlyBreakdown;
import com.isotrol.impe3.web20.model.FavoriteEntity;
import com.isotrol.impe3.web20.model.GroupEntity;
import com.isotrol.impe3.web20.model.LogTableEntity;
import com.isotrol.impe3.web20.model.MemberEntity;
import com.isotrol.impe3.web20.model.MembershipEntity;
import com.isotrol.impe3.web20.model.NoticeEntity;
import com.isotrol.impe3.web20.model.RatingEntity;
import com.isotrol.impe3.web20.model.RatingEventEntity;
import com.isotrol.impe3.web20.model.RecommendationEntity;
import com.isotrol.impe3.web20.model.RelationshipEntity;
import com.isotrol.impe3.web20.model.ResourceEntity;
import com.isotrol.impe3.web20.model.SequenceEntity;
import com.isotrol.impe3.web20.model.SourceEntity;
import com.isotrol.impe3.web20.model.TagEntity;
import com.isotrol.impe3.web20.model.TagNameEntity;
import com.isotrol.impe3.web20.model.TagSetEntity;
import com.isotrol.impe3.web20.server.CommentInternalFilterDTO;
import com.isotrol.impe3.web20.server.CommunityManager;
import com.isotrol.impe3.web20.server.CounterKey;
import com.isotrol.impe3.web20.server.GroupKey;
import com.isotrol.impe3.web20.server.LogTableFilterDTO;
import com.isotrol.impe3.web20.server.RatingFilterDTO;
import com.isotrol.impe3.web20.server.Relationship;
import com.isotrol.impe3.web20.server.TagKey;


/**
 * General DAO implementation for Web 2.0 server.
 * @author Emilio Escobar Reyero
 */
@Repository
public class DAOImpl extends com.isotrol.impe3.hib.dao.DAOImpl implements DAO {
	public DAOImpl() {
	}

	/**
	 * @see com.isotrol.impe3.web20.dao.DAO#findMembers(com.isotrol.impe3.web20.api.MemberFilterDTO,
	 * com.google.common.base.Function)
	 */
	public <D> PageDTO<D> findMembers(PageFilter<MemberFilterDTO> filter,
		Function<? super MemberEntity, ? extends D> transformer) {
		checkNotNull(filter, "A page filter for members must be provided");
		checkNotNull(filter.getFilter(), "A search filter for members must be provided");
		checkNotNull(transformer, "A transformation function for members must be provided");
		final Criteria count = getMemberCriteria(filter);
		final Criteria select = getMemberCriteria(filter);
		orderSelect(select, filter.getOrderings());
		return PageSupport.getPage(count, select, filter.getPagination(), transformer);
	}

	/**
	 * @see com.isotrol.impe3.web20.dao.DAO#getMemberMemberships(com.isotrol.impe3.web20.api.MembershipSelFilterDTO,
	 * com.google.common.base.Function)
	 */
	public <D> PageDTO<D> getMemberMemberships(PageFilter<MembershipSelFilterDTO> filter,
		Function<? super MembershipEntity, ? extends D> transformer) {
		checkNotNull(filter, "A page filter for members must be provided");
		checkNotNull(filter.getFilter(), "A search filter for members must be provided");
		checkNotNull(filter.getFilter().getId(), "A member uuid must be provided");
		checkNotNull(transformer, "A transformation function for members must be provided");
		final Criteria count = getMemberMembershipCriteria(filter);
		final Criteria select = getMemberMembershipCriteria(filter);
		orderSelect(select, filter.getOrderings());
		return PageSupport.getPage(count, select, filter.getPagination(), transformer);
	}

	/**
	 * Create a new criteria for member memberships.
	 * @param filter Member memberships filter.
	 * @return The created criteria.
	 */
	private Criteria getMemberMembershipCriteria(PageFilter<MembershipSelFilterDTO> filter) {
		Criteria c = newCriteria(MembershipEntity.class);
		c.add(Restrictions.eq("member.id", filter.getFilter().getId()));
		if (filter.getFilter().getRole() != null) {
			c.add(Restrictions.eq("role", filter.getFilter().getRole()));
		}

		c.createAlias("community", "c", Criteria.LEFT_JOIN);
		c.add(Restrictions.eq("c.deleted", false));
		if (!filter.getFilter().isGlobal()) {
			c.add(Restrictions.ne("c.id", CommunityManager.GLOBAL_ID));
		}
		c.add(Restrictions.isNull("deletion"));
		if (filter.getFilter().isValidated() == null) {
			// nothing here.
		} else if (filter.getFilter().isValidated().equals(Boolean.FALSE)) {
			c.add(Restrictions.isNull("validation"));
		} else {
			c.add(Restrictions.isNotNull("validation"));
		}
		return c;
	}

	/**
	 * Create a new criteria for members.
	 * @param filter Member filter.
	 * @return The created criteria.
	 */
	private Criteria getMemberCriteria(PageFilter<MemberFilterDTO> filter) {
		Criteria c = newCriteria(MemberEntity.class);
		c.add(Restrictions.eq("deleted", Boolean.FALSE));
		if (filter.getFilter().getCommunityId() != null) {
			c.createAlias("memberships", "ms", Criteria.LEFT_JOIN);
			c.add(Restrictions.eq("ms.community.id", filter.getFilter().getCommunityId()));
			c.add(Restrictions.isNull("ms.deletion"));
			c.add(Restrictions.isNotNull("ms.validation"));
		}
		FilterSupport.add(c, "memberCode", filter.getFilter().getMemberCode());
		FilterSupport.add(c, "name", filter.getFilter().getName());
		FilterSupport.add(c, "displayName", filter.getFilter().getDisplayName());
		return c;
	}

	/**
	 * @see com.isotrol.impe3.web20.dao.DAO#getMemberByCode(java.lang.String)
	 */
	public MemberEntity getMemberByCode(String code) {
		Preconditions.checkNotNull(code);
		return unique(MemberEntity.class, getNamedQuery(MemberEntity.BY_CODE).setString(0, code));
	}

	/**
	 * @see com.isotrol.impe3.web20.dao.DAO#getMemberByName(java.lang.String)
	 */
	public MemberEntity getMemberByName(String name) {
		Preconditions.checkNotNull(name);
		return unique(MemberEntity.class, getNamedQuery(MemberEntity.BY_NAME).setString(0, name));
	}

	/**
	 * @see com.isotrol.impe3.web20.dao.DAO#findCommunities(com.isotrol.impe3.web20.api.CommunityFilterDTO,
	 * com.google.common.base.Function)
	 */
	public <D> PageDTO<D> findCommunities(PageFilter<CommunityFilterDTO> filter,
		Function<? super CommunityEntity, ? extends D> transformer) {
		checkNotNull(filter, "A page filter for communities must be provided");
		checkNotNull(filter.getFilter(), "A search filter for communities must be provided");
		checkNotNull(transformer, "A transformation function for communities must be provided");
		final Criteria count = getCommunityCriteria(filter);
		final Criteria select = getCommunityCriteria(filter);
		orderSelect(select, filter.getOrderings());
		return PageSupport.getPage(count, select, filter.getPagination(), transformer);
	}

	/**
	 * @see com.isotrol.impe3.web20.dao.DAO#getMembership(java.util.UUID, java.util.UUID)
	 */
	public MembershipEntity getMembership(UUID memberId, UUID communityId) {
		return unique(MembershipEntity.class, getNamedQuery(MembershipEntity.MC).setParameter(0, memberId)
			.setParameter(1, communityId));
	}

	public MembershipEntity getMembershipDeleted(UUID memberId, UUID communityId) {
		return unique(MembershipEntity.class, getNamedQuery(MembershipEntity.MCD).setParameter(0, memberId)
			.setParameter(1, communityId));
	}

	/**
	 * @see com.isotrol.impe3.web20.dao.DAO#getValidatedMembership(java.util.UUID, java.util.UUID)
	 */
	public MembershipEntity getValidatedMembership(UUID memberId, UUID communityId) {
		return unique(MembershipEntity.class, getNamedQuery(MembershipEntity.VMC).setParameter(0, memberId)
			.setParameter(1, communityId));
	}

	/**
	 * Create a new criteria for communities.
	 * @param filter Community filter.
	 * @return The created criteria.
	 */
	private Criteria getCommunityCriteria(PageFilter<CommunityFilterDTO> filter) {
		Criteria c = newCriteria(CommunityEntity.class);
		c.add(Restrictions.ne("id", CommunityManager.GLOBAL_STR_ID));
		c.add(Restrictions.eq("deleted", Boolean.FALSE));

		if (filter.getFilter().getName() != null && filter.getFilter().getDescription() != null) {
			c.add(Restrictions.or(FilterSupport.criterion("name", filter.getFilter().getName()),
				FilterSupport.criterion("description", filter.getFilter().getDescription())));
		} else if (filter.getFilter().getName() != null || filter.getFilter().getDescription() != null) {
			FilterSupport.add(c, "name", filter.getFilter().getName());
			FilterSupport.add(c, "description", filter.getFilter().getDescription());
		}
		return c;
	}

	/**
	 * @see com.isotrol.impe3.web20.dao.DAO#getCommunityByCode(java.lang.String)
	 */
	public CommunityEntity getCommunityByCode(String code) {
		Preconditions.checkNotNull(code);
		return unique(CommunityEntity.class, getNamedQuery(CommunityEntity.BY_CODE).setString(0, code));
	}

	/**
	 * @see com.isotrol.impe3.web20.dao.DAO#getCommunityMemberships(com.isotrol.impe3.web20.api.CommunityMembersFilterDTO,
	 * com.google.common.base.Function)
	 */
	public <D> PageDTO<D> getCommunityMemberships(PageFilter<CommunityMembersFilterDTO> filter,
		Function<? super MembershipEntity, ? extends D> transformer) {
		checkNotNull(filter, "A page filter for coummunities must be provided");
		checkNotNull(filter.getFilter(), "A search filter for coummunities must be provided");
		checkNotNull(filter.getFilter().getId(), "A community uuid must be provided");
		checkNotNull(transformer, "A transformation function for communities must be provided");
		final Criteria count = getCommunityMembershipCriteria(filter);
		final Criteria select = getCommunityMembershipCriteria(filter);
		orderSelect(select, filter.getOrderings());
		return PageSupport.getPage(count, select, filter.getPagination(), transformer);
	}

	private Criteria getCommunityMembershipCriteria(PageFilter<CommunityMembersFilterDTO> filter) {
		Criteria c = newCriteria(MembershipEntity.class);
		c.add(Restrictions.eq("community.id", filter.getFilter().getId()));
		c.createAlias("member", "m", Criteria.LEFT_JOIN);
		c.add(Restrictions.eq("m.deleted", false));
		c.add(Restrictions.isNull("deletion"));
		if (filter.getFilter().isValidated() == null) {
			// nothing here.
		} else if (filter.getFilter().isValidated().equals(Boolean.FALSE)) {
			c.add(Restrictions.isNull("validation"));
		} else {
			c.add(Restrictions.isNotNull("validation"));
		}
		if (filter.getFilter().getRole() != null) {
			c.add(Restrictions.eq("role", filter.getFilter().getRole()));
		}

		return c;
	}

	/**
	 * @see com.isotrol.impe3.web20.dao.DAO#getResourceById(java.lang.String)
	 */
	public ResourceEntity getResourceById(String resourceId) {
		checkNotNull(resourceId);
		return unique(ResourceEntity.class, ResourceEntity.BY_ID, resourceId);
	}

	/**
	 * @see com.isotrol.impe3.web20.dao.DAO#getRelationship(com.isotrol.impe3.web20.server.Relationship)
	 */
	public RelationshipEntity getRelationship(Relationship key) {
		checkNotNull(key);
		final Long g = key.getGroup();
		if (g == null) {
			return unique(RelationshipEntity.class, RelationshipEntity.WOG, key.getResourceId());
		} else {
			return unique(RelationshipEntity.class,
				getNamedQuery(RelationshipEntity.WG).setParameter(0, key.getResourceId()).setParameter(1, g));
		}
	}

	/**
	 * @see com.isotrol.impe3.web20.dao.DAO#getCounterType(java.lang.String)
	 */
	public CounterTypeEntity getCounterType(String name) {
		checkNotNull(name);
		return unique(CounterTypeEntity.class, CounterTypeEntity.BY_NAME, name);
	}

	/**
	 * @see com.isotrol.impe3.web20.dao.DAO#getAggregation(java.lang.String)
	 */
	public AggregationEntity getAggregation(String name) {
		checkNotNull(name);
		return unique(AggregationEntity.class, AggregationEntity.BY_NAME, name);
	}

	/**
	 * @see com.isotrol.impe3.web20.dao.DAO#getGroup(com.isotrol.impe3.web20.server.GroupKey)
	 */
	public GroupEntity getGroup(GroupKey key) {
		checkNotNull(key);
		final Long a = key.getAggregation();
		if (a == null) {
			return unique(GroupEntity.class, GroupEntity.WOA, key.getCommunity());
		} else {
			return unique(GroupEntity.class, getNamedQuery(GroupEntity.WA).setParameter(0, key.getCommunity())
				.setParameter(1, a));
		}
	}

	/**
	 * @see com.isotrol.impe3.web20.dao.DAO#getCounter(com.isotrol.impe3.web20.server.CounterKey)
	 */
	public CounterEntity getCounter(CounterKey key) {
		return unique(CounterEntity.class,
			getNamedQuery(CounterEntity.TR).setLong(0, key.getCounterType()).setLong(1, key.getRelationship()));
	}

	/**
	 * @see com.isotrol.impe3.web20.dao.DAO#incrementCounter(long)
	 */
	public void incrementCounter(long counterId) {
		getNamedQuery(CounterEntity.INC).setLong(0, counterId).executeUpdate();
	}

	/**
	 * Increments a breakdown counter.
	 * @param query Named query to execute.
	 * @param key Counter key.
	 * @return Number of updated counters.
	 */
	private int incrementBreakdownCounter(String query, CounterPK key) {
		return getNamedQuery(query).setParameter(0, key.getCounter()).setLong(1, key.getTimestamp()).executeUpdate();
	}

	/**
	 * @see com.isotrol.impe3.web20.dao.DAO#incrementHourlyCounter(com.isotrol.impe3.web20.model.CounterPK)
	 */
	public int incrementHourlyCounter(CounterPK key) {
		return incrementBreakdownCounter(CounterHourlyBreakdown.INC, key);
	}

	/**
	 * @see com.isotrol.impe3.web20.dao.DAO#incrementDailyCounter(com.isotrol.impe3.web20.model.CounterPK)
	 */
	public int incrementDailyCounter(CounterPK key) {
		return incrementBreakdownCounter(CounterDailyBreakdown.INC, key);
	}

	/**
	 * @see com.isotrol.impe3.web20.dao.DAO#incrementMonthlyCounter(com.isotrol.impe3.web20.model.CounterPK)
	 */
	public int incrementMonthlyCounter(CounterPK key) {
		return incrementBreakdownCounter(CounterMonthlyBreakdown.INC, key);
	}

	/**
	 * @see com.isotrol.impe3.web20.dao.DAO#incrementYearlyCounter(com.isotrol.impe3.web20.model.CounterPK)
	 */
	public int incrementYearlyCounter(CounterPK key) {
		return incrementBreakdownCounter(CounterYearlyBreakdown.INC, key);
	}

	/**
	 * @see com.isotrol.impe3.web20.dao.DAO#getAllCounters()
	 */
	public List<Object[]> getAllCounters() {
		return list(Object[].class, getNamedQuery(CounterEntity.ALL));
	}

	/**
	 * @see com.isotrol.impe3.web20.dao.DAO#getAllCountersFromTimestamp(long)
	 */
	public List<Object[]> getAllCountersFromTimestamp(long timestamp) {
		return list(Object[].class, getNamedQuery(CounterEventEntity.ALL).setParameter(0, timestamp));
	}

	/**
	 * @see com.isotrol.impe3.web20.dao.DAO#getSourceByMember(java.util.UUID)
	 */
	public SourceEntity getSourceByMember(UUID memberId) {
		return unique(SourceEntity.class, SourceEntity.MEMBER, memberId);
	}

	/**
	 * @see com.isotrol.impe3.web20.dao.DAO#getSourceByOrigin(java.lang.String)
	 */
	public SourceEntity getSourceByOrigin(String origin) {
		return unique(SourceEntity.class, SourceEntity.ORIGIN, origin);
	}

	/**
	 * @see com.isotrol.impe3.web20.dao.DAO#getRating(long)
	 */
	public RatingEntity getRating(long relationshipId) {
		return unique(RatingEntity.class, RatingEntity.REL, relationshipId);
	}

	/**
	 * @see com.isotrol.impe3.web20.dao.DAO#getAllRatings()
	 */
	public List<Object[]> getAllRatings() {
		return list(Object[].class, getNamedQuery(RatingEntity.ALL));
	}

	/**
	 * @see com.isotrol.impe3.web20.dao.DAO#getAllRatingsFromTimestamp(long)
	 */
	public List<Object[]> getAllRatingsFromTimestamp(long timestamp) {
		return list(Object[].class, getNamedQuery(RatingEventEntity.ALL).setParameter(0, timestamp));
	}

	/**
	 * @see com.isotrol.impe3.web20.dao.DAO#getTagSet(java.lang.String)
	 */
	public TagSetEntity getTagSet(String name) {
		checkNotNull(name);
		return unique(TagSetEntity.class, TagSetEntity.BY_NAME, name);
	}

	/**
	 * @see com.isotrol.impe3.web20.dao.DAO#getTagName(java.lang.String)
	 */
	public TagNameEntity getTagName(String name) {
		checkNotNull(name);
		return unique(TagNameEntity.class, TagNameEntity.BY_NAME, name);
	}

	/**
	 * @see com.isotrol.impe3.web20.dao.DAO#getTag(com.isotrol.impe3.web20.server.TagKey)
	 */
	public TagEntity getTag(TagKey key) {
		checkNotNull(key);
		return unique(TagEntity.class,
			getNamedQuery(TagEntity.BY_KEY).setLong(0, key.getSet()).setLong(1, key.getName()));
	}

	/**
	 * @see com.isotrol.impe3.web20.dao.DAO#findRatingEvents(com.isotrol.impe3.web20.server.RatingFilterDTO)
	 */
	@SuppressWarnings("unchecked")
	public List<RatingEventEntity> findRatingEvents(RatingFilterDTO filter) {
		Criteria c = newCriteria(RatingEventEntity.class);
		c.add(Restrictions.eq("source.id", filter.getSource()));
		c.createAlias("ratings", "r", Criteria.LEFT_JOIN);
		c.add(Restrictions.eq("r.id", filter.getRating()));
		return c.list();
	}

	/**
	 * @see com.isotrol.impe3.web20.dao.DAO#loadAppliedTags()
	 */
	public List<TagEntity> loadAppliedTags() {
		return list(TagEntity.class, getNamedQuery(TagEntity.USED));
	}

	/**
	 * @see com.isotrol.impe3.web20.dao.DAO#loadTagsBySetName(java.lang.String)
	 */
	public List<TagEntity> loadTagsBySetName(String set) {
		checkNotNull(set);
		return list(TagEntity.class, TagEntity.BY_SET_NAME, set);
	}

	public int numberOfComments(Long relationshipId) throws ServiceException {
		final Criteria c = newCriteria(CommentEntity.class);
		c.add(Restrictions.eq("deleted", Boolean.FALSE));
		c.add(Restrictions.eq("valid", Boolean.TRUE));
		c.createAlias("relationship", "rs", Criteria.LEFT_JOIN);
		c.add(Restrictions.eq("rs.id", relationshipId));

		return c.list().size();
	}

	/**
	 * @see com.isotrol.impe3.web20.dao.DAO#findComments(com.isotrol.impe3.web20.api.CommentFilterDTO,
	 * com.google.common.base.Function)
	 */
	public <D> PageDTO<D> findComments(CommentInternalFilterDTO filter,
		Function<? super CommentEntity, ? extends D> transformer) {
		checkNotNull(filter, "A search filter for comments must be provided");
		checkNotNull(transformer, "A transformation function for comments must be provided");
		final Criteria count = getCommentCriteria(filter);
		final Criteria select = getCommentCriteria(filter);
		orderSelect(select, filter.getOrderings());
		return PageSupport.getPage(count, select, filter.getPagination(), transformer);
	}

	private Criteria getCommentCriteria(CommentInternalFilterDTO filter) {
		Criteria c = newCriteria(CommentEntity.class);
		c.add(Restrictions.eq("deleted", Boolean.FALSE));

		if (filter.isValid() != null) {
			if (filter.isValid()) {
				c.add(Restrictions.eq("valid", Boolean.TRUE));
			} else {
				c.add(Restrictions.eq("valid", Boolean.FALSE));
			}
		}

		if (filter.isModerated() != null) {
			if (filter.isModerated()) {
				c.add(Restrictions.isNotNull("lastModeration"));
			} else {
				c.add(Restrictions.isNull("lastModeration"));
			}
		}

		if (filter.getRelationship() != null) {
			c.createAlias("relationship", "rs", Criteria.LEFT_JOIN);
			c.add(Restrictions.eq("rs.id", filter.getRelationship()));
		}

		final Calendar dateInit = Calendar.getInstance();
		final Calendar dateEnd = Calendar.getInstance();

		if (filter.getLowDate() != null && filter.getHighDate() != null) {
			dateInit.setTime(filter.getLowDate());
			dateEnd.setTime(filter.getHighDate());
			c.add(Restrictions.between("date", dateInit, dateEnd));
		} else if (filter.getLowDate() != null) {
			dateInit.setTime(filter.getLowDate());
			c.add(Restrictions.ge("date", dateInit));
		} else if (filter.getHighDate() != null) {
			dateEnd.setTime(filter.getHighDate());
			c.add(Restrictions.le("date", dateEnd));
		}

		return c;
	}

	/**
	 * @see com.isotrol.impe3.web20.dao.DAO#loadComments()
	 */
	public List<Object[]> loadComments() {
		return list(Object[].class, getNamedQuery(CommentEntity.ALL));
	}

	/**
	 * @see com.isotrol.impe3.web20.dao.DAO#loadComments(long)
	 */
	public List<Object[]> loadComments(long timestamp) {
		return list(Object[].class, getNamedQuery(CommentEntity.ALL_TIMED).setParameter(0, timestamp));
	}

	/**
	 * @throws SequenceNotFoundException
	 * @see com.isotrol.impe3.web20.dao.DAO#getNextValue(java.lang.String)
	 */
	public synchronized long getNextValue(String id) throws SequenceNotFoundException {
		SequenceEntity seq = (SequenceEntity) getSession().get(SequenceEntity.class, id, LockOptions.UPGRADE);
		if (seq == null) {
			seq = new SequenceEntity(id, 0);
			try {
				getSession().save(seq);
				flush();
			} catch (Exception e) {
				seq = (SequenceEntity) getSession().get(SequenceEntity.class, id, LockOptions.UPGRADE);
				if (seq == null) {
					throw new SequenceNotFoundException(id);
				}
			}
		}

		return nextValue(seq);
	}

	private long nextValue(SequenceEntity entity) {
		final long next = entity.getNext();
		getSession().update(entity);
		return next;
	}

	/**
	 * @see com.isotrol.impe3.web20.dao.DAO#findBatch(com.isotrol.impe3.web20.server.LogTableFilterDTO,
	 * com.google.common.base.Function)
	 */
	public <D> List<D> findBatch(LogTableFilterDTO filter, Function<? super LogTableEntity, ? extends D> transformer) {
		Query query = getNamedQuery(LogTableEntity.BY_NAME).setString(0, filter.getName())
			.setLong(1, filter.getFirst()).setMaxResults(filter.getSize());

		final List<LogTableEntity> entities = list(LogTableEntity.class, query);

		return Lists.newArrayList(Iterables.transform(entities, transformer));
	}

	/**
	 * @see com.isotrol.impe3.web20.dao.DAO#findNotices(java.util.UUID, com.google.common.base.Function)
	 */
	public <D> PageDTO<D> findNotices(NoticeFilterDTO filter, Function<? super NoticeEntity, ? extends D> transformer) {
		checkNotNull(filter, "A filter must be provided");
		checkNotNull(transformer, "A transformation function for members must be provided");
		final Criteria count = getNoticeCriteria(filter);
		final Criteria select = getNoticeCriteria(filter);
		orderSelect(select, filter.getOrderings());
		return PageSupport.getPage(count, select, filter.getPagination(), transformer);
	}

	/**
	 * Create a new criteria for community notices
	 * @param filter Community notices filter.
	 * @return The created criteria.
	 */
	private Criteria getNoticeCriteria(NoticeFilterDTO filter) {
		Criteria c = newCriteria(NoticeEntity.class);
		c.add(Restrictions.eq("community.id", filter.getCommunityId()));
		c.add(Restrictions.eq("deleted", false));

		if (filter.getTitle() != null && filter.getDescription() != null) {
			c.add(Restrictions.or(FilterSupport.criterion("title", filter.getTitle()),
				FilterSupport.criterion("description", filter.getDescription())));
		} else if (filter.getTitle() != null || filter.getDescription() != null) {
			FilterSupport.add(c, "title", filter.getTitle());
			FilterSupport.add(c, "description", filter.getDescription());
		}

		if (filter.isActive() == null) {
			// nothing, return all.
		} else if (filter.isActive()) {
			// true released and not expired
			c.add(Restrictions.or(Restrictions.isNull("expiration"),
				Restrictions.gt("expiration", Calendar.getInstance())));
			c.add(Restrictions.le("release", Calendar.getInstance()));
		} else {
			// false not released or expired
			c.add(Restrictions.or(
				Restrictions.or(Restrictions.isNull("release"), Restrictions.gt("release", Calendar.getInstance())),
				Restrictions.le("expiration", Calendar.getInstance())));
		}
		return c;
	}

	private void orderSelect(Criteria c, List<OrderDTO> orderings) {
		if (orderings != null && c != null) {
			for (OrderDTO order : orderings) {
				c.addOrder(order.getAscending() ? Order.asc(order.getName()) : Order.desc(order.getName()));
			}
		}
	}

	/**
	 * @see com.isotrol.impe3.web20.dao.DAO#findFavorites(com.isotrol.impe3.web20.api.FavoriteFilterDTO,
	 * com.google.common.base.Function)
	 */
	public <D> PageDTO<D> findFavorites(FavoriteFilterDTO filter,
		Function<? super FavoriteEntity, ? extends D> transformer) {
		checkNotNull(filter, "A filter must be provided");
		checkNotNull(transformer, "A transformation function for favorites must be provided");
		final Criteria count = getFavoriteCriteria(filter);
		final Criteria select = getFavoriteCriteria(filter);
		orderSelect(select, filter.getOrderings());
		return PageSupport.getPage(count, select, filter.getPagination(), transformer);
	}

	private Criteria getFavoriteCriteria(FavoriteFilterDTO filter) {
		Criteria c = newCriteria(FavoriteEntity.class);
		c.add(Restrictions.eq("member.id", filter.getMember()));
		c.createAlias("relationship", "r", Criteria.LEFT_JOIN);
		c.createAlias("r.group", "g", Criteria.LEFT_JOIN);
		c.add(Restrictions.eq("g.id", filter.getGroup()));
		return c;
	}

	public <D> PageDTO<D> findRecommendations(RecommendationFilterDTO filter,
		Function<? super RecommendationEntity, ? extends D> transformer) {
		checkNotNull(filter, "A filter must be provided");
		checkNotNull(transformer, "A transformation function for recommendations must be provided");
		final Criteria count = getRecommendationCriteria(filter);
		final Criteria select = getRecommendationCriteria(filter);
		orderSelect(select, filter.getOrderings());
		return PageSupport.getPage(count, select, filter.getPagination(), transformer);
	}

	private Criteria getRecommendationCriteria(RecommendationFilterDTO filter) {
		Criteria c = newCriteria(RecommendationEntity.class);

		if (filter.getRecommended() != null) {
			c.add(Restrictions.eq("recommended.id", filter.getRecommended()));
		}
		if (filter.getRecommender() != null) {
			c.add(Restrictions.eq("recommender.id", filter.getRecommender()));
		}

		c.createAlias("relationship", "r", Criteria.LEFT_JOIN);
		c.createAlias("r.group", "g", Criteria.LEFT_JOIN);
		c.add(Restrictions.eq("g.id", filter.getGroup()));
		return c;
	}

	private Map<UUID, Integer> toMap(Iterable<Object[]> rows) {
		final Map<UUID, Integer> map = Maps.newHashMap();
		for (Object[] row : rows) {
			map.put((UUID) row[0], ((Number) row[1]).intValue());
		}
		return map;
	}

	/**
	 * @see com.isotrol.impe3.web20.dao.DAO#getValidatedMembers()
	 */
	public Map<UUID, Integer> getValidatedMembers() {
		return toMap(list(Object[].class, getNamedQuery(MembershipEntity.COUNT_VM)));
	}

	/**
	 * @see com.isotrol.impe3.web20.dao.DAO#getNonValidatedMembers()
	 */
	public Map<UUID, Integer> getNonValidatedMembers() {
		return toMap(list(Object[].class, getNamedQuery(MembershipEntity.COUNT_NVM)));
	}

	public CommunityEntity findCommunityByLastCode(String lastCode) {
		Preconditions.checkNotNull(lastCode);
		final List<CommunityEntity> communities = list(CommunityEntity.class,
			getNamedQuery(CommunityEntity.BY_LASTCODE).setString(0, lastCode));
		return communities != null && !communities.isEmpty() ? communities.get(0) : null;
	}

	public MemberEntity findMemberByLastCode(String lastCode) {
		Preconditions.checkNotNull(lastCode);
		final List<MemberEntity> members = list(MemberEntity.class,
			getNamedQuery(MemberEntity.BY_LASTCODE).setString(0, lastCode));
		return members != null && !members.isEmpty() ? members.get(0) : null;
	}

	public <D> PageDTO<D> findMembersWithDeleted(PaginationDTO pagination,
		Function<? super MemberEntity, ? extends D> transformer) {
		checkNotNull(pagination, "A pagination for members must be provided");
		checkNotNull(transformer, "A transformation function for members must be provided");
		final Criteria count = newCriteria(MemberEntity.class);
		final Criteria select = newCriteria(MemberEntity.class);
		orderSelect(select, Lists.<OrderDTO> newArrayList(new OrderDTO("id", true)));
		return PageSupport.getPage(count, select, pagination, transformer);
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> findMembersNoGlobal() {
		final String sql = "select m.ID from WEB20_MEMBER m left outer join WEB20_MEMBERSHIP s on m.ID = s.MMSP_MMBR_ID and s.MMSP_CMTY_ID = '"
			+ CommunityManager.GLOBAL_STR_ID + "'";
		return getSession().createSQLQuery(sql).list();
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> getRWUC(int max) throws ServiceException {
		return getNamedQuery(CommentEntity.RWUC).setMaxResults(2 * Math.max(1, max)).list();
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> getRWUCInCommunity(UUID communityId, int max) throws ServiceException {
		return getNamedQuery(CommentEntity.RWUC_IC).setParameter(0, communityId).setMaxResults(Math.max(1, max)).list();
	}

	public int countRatesByCommentAndSource(Long commentId, Long sourceId) throws ServiceException {
		checkNotNull(commentId, "A comment id must be provided");
		checkNotNull(sourceId, "A source id must be provided");

		final Criteria count = newCriteria(CommentRateEntity.class);
		count.createAlias("comment", "c", Criteria.LEFT_JOIN);
		count.createAlias("source", "s", Criteria.LEFT_JOIN);
		count.add(Restrictions.eq("c.id", commentId));
		count.add(Restrictions.eq("s.id", sourceId));

		final List<?> list = count.list();
		return list == null ? 0 : list.size();
	}

}
