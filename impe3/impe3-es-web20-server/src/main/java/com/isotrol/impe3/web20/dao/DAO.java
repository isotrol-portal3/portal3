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
package com.isotrol.impe3.web20.dao;


import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.common.base.Function;
import com.isotrol.impe3.dto.PageDTO;
import com.isotrol.impe3.dto.PageFilter;
import com.isotrol.impe3.dto.PaginationDTO;
import com.isotrol.impe3.dto.ServiceException;
import com.isotrol.impe3.web20.api.CommunityFilterDTO;
import com.isotrol.impe3.web20.api.CommunityMembersFilterDTO;
import com.isotrol.impe3.web20.api.FavoriteFilterDTO;
import com.isotrol.impe3.web20.api.MemberFilterDTO;
import com.isotrol.impe3.web20.api.MembershipSelFilterDTO;
import com.isotrol.impe3.web20.api.NoticeFilterDTO;
import com.isotrol.impe3.web20.api.RecommendationFilterDTO;
import com.isotrol.impe3.web20.api.SequenceNotFoundException;
import com.isotrol.impe3.web20.model.AggregationEntity;
import com.isotrol.impe3.web20.model.CommentEntity;
import com.isotrol.impe3.web20.model.CommunityEntity;
import com.isotrol.impe3.web20.model.CounterEntity;
import com.isotrol.impe3.web20.model.CounterPK;
import com.isotrol.impe3.web20.model.CounterTypeEntity;
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
import com.isotrol.impe3.web20.model.SourceEntity;
import com.isotrol.impe3.web20.model.TagEntity;
import com.isotrol.impe3.web20.model.TagNameEntity;
import com.isotrol.impe3.web20.model.TagSetEntity;
import com.isotrol.impe3.web20.server.CommentInternalFilterDTO;
import com.isotrol.impe3.web20.server.CounterKey;
import com.isotrol.impe3.web20.server.GroupKey;
import com.isotrol.impe3.web20.server.LogTableFilterDTO;
import com.isotrol.impe3.web20.server.RatingFilterDTO;
import com.isotrol.impe3.web20.server.Relationship;
import com.isotrol.impe3.web20.server.TagKey;


/**
 * General DAO for Web 2.0 server.
 * @author Emilio Escobar Reyero
 */
public interface DAO extends com.isotrol.impe3.hib.dao.DAO {
	/**
	 * Find members.
	 * @param <D> Output type.
	 * @param filter Search filter.
	 * @param transformer Transformation function.
	 * @return A page of results.
	 */
	<D> PageDTO<D> findMembers(PageFilter<MemberFilterDTO> filter,
		Function<? super MemberEntity, ? extends D> transformer);

	/**
	 * Recover member memberships
	 * @param <D> Output type.
	 * @param filter Search filter.
	 * @param transformer Transformation function.
	 * @return A page of results.
	 */
	<D> PageDTO<D> getMemberMemberships(PageFilter<MembershipSelFilterDTO> filter,
		Function<? super MembershipEntity, ? extends D> transformer);

	/**
	 * Finds a member by membercode.
	 * @param code Code.
	 * @return The requested member or {@code null} if it is not found.
	 */
	MemberEntity getMemberByCode(String code);

	/**
	 * Finds a member by membername.
	 * @param name Name.
	 * @return The requested member or {@code null} if it is not found.
	 */
	MemberEntity getMemberByName(String name);

	/**
	 * Finds a membership.
	 * @param memberId Member Id.
	 * @param communityId Community Id.
	 * @return The requested membership or {@code null} if it is not found.
	 */
	MembershipEntity getMembership(UUID memberId, UUID communityId);

	MembershipEntity getMembershipDeleted(UUID memberId, UUID communityId);

	/**
	 * Finds a validated membership.
	 * @param memberId Member Id.
	 * @param communityId Community Id.
	 * @return The requested validated membership or {@code null} if it is not found.
	 */
	MembershipEntity getValidatedMembership(UUID memberId, UUID communityId);

	/**
	 * Find communities.
	 * @param <D> Output type.
	 * @param filter Search filter.
	 * @param transformer Transformation function.
	 * @return A page of results.
	 */
	<D> PageDTO<D> findCommunities(PageFilter<CommunityFilterDTO> filter,
		Function<? super CommunityEntity, ? extends D> transformer);

	/**
	 * Finds a community by communitycode.
	 * @param code Code.
	 * @return The requested community or {@code null} if it is not found.
	 */
	CommunityEntity getCommunityByCode(String code);

	/**
	 * Recover community memberships
	 * @param <D> Output type.
	 * @param filter Search filter.
	 * @param transformer Transformation function.
	 * @return A page of results.
	 */
	<D> PageDTO<D> getCommunityMemberships(PageFilter<CommunityMembersFilterDTO> filter,
		Function<? super MembershipEntity, ? extends D> transformer);

	/**
	 * Finds a resource by resource id
	 * @param resourceId Resource Id.
	 * @return The requested resource or {@code null} if it is not found.
	 */
	ResourceEntity getResourceById(String resourceId);

	/**
	 * Finds a relationship
	 * @param key Relationship key.
	 * @return The requested relationship or {@code null} if it is not found.
	 */
	RelationshipEntity getRelationship(Relationship key);

	/**
	 * Finds a counter type by name.
	 * @param name Counter type name.
	 * @return The requested counter type or {@code null} if it is not found.
	 */
	CounterTypeEntity getCounterType(String name);

	/**
	 * Finds an aggregation by name.
	 * @param name Aggregation name.
	 * @return The requested aggregation or {@code null} if it is not found.
	 */
	AggregationEntity getAggregation(String name);

	/**
	 * Finds a group by key.
	 * @param key Group key.
	 * @return The requested group or {@code null} if it is not found.
	 */
	GroupEntity getGroup(GroupKey key);

	/**
	 * Finds a counter by key.
	 * @param key Counter key.
	 * @return The requested counter or {@code null} if it is not found.
	 */
	CounterEntity getCounter(CounterKey key);

	/**
	 * Increments a counter.
	 * @param counterId Counter id.
	 */
	void incrementCounter(long counterId);

	/**
	 * Increments an hourly counter.
	 * @param key Counter Key.
	 * @return Number of updated counters.
	 */
	int incrementHourlyCounter(CounterPK key);

	/**
	 * Increments an daily counter.
	 * @param key Counter Key.
	 * @return Number of updated counters.
	 */
	int incrementDailyCounter(CounterPK key);

	/**
	 * Increments an monthly counter.
	 * @param key Counter Key.
	 * @return Number of updated counters.
	 */
	int incrementMonthlyCounter(CounterPK key);

	/**
	 * Increments an tearly counter.
	 * @param key Counter Key.
	 * @return Number of updated counters.
	 */
	int incrementYearlyCounter(CounterPK key);

	/**
	 * Returns every counter.
	 * @return Every counter.
	 */
	List<Object[]> getAllCounters();

	/**
	 * Returns every counter from a certain timestamp.
	 * @param timestamp Starting timestamp.
	 * @return Every counter from the specified timestamp.
	 */
	List<Object[]> getAllCountersFromTimestamp(long timestamp);

	/**
	 * Finds a source by member.
	 * @param memberId Member Id.
	 * @return The requested source or {@code null} if it is not found.
	 */
	SourceEntity getSourceByMember(UUID memberId);

	/**
	 * Finds a source by origin.
	 * @param origin Request origin.
	 * @return The requested source or {@code null} if it is not found.
	 */
	SourceEntity getSourceByOrigin(String origin);

	/**
	 * Finds a rating.
	 * @param relationshipId Relationship id.
	 * @return The requested rating or {@code null} if it is not found.
	 */
	RatingEntity getRating(long relationshipId);

	/**
	 * Returns every rating.
	 * @return Every rating.
	 */
	List<Object[]> getAllRatings();

	/**
	 * Returns every rating from a certain timestamp.
	 * @param timestamp Starting timestamp.
	 * @return Every rating from the specified timestamp.
	 */
	List<Object[]> getAllRatingsFromTimestamp(long timestamp);

	/**
	 * Finds a tag set.
	 * @param name Tag set name.
	 * @return The requested tag set or {@code null} if it is not found.
	 */
	TagSetEntity getTagSet(String name);

	/**
	 * Finds a tag name.
	 * @param name Tag name.
	 * @return The requested tag name or {@code null} if it is not found.
	 */
	TagNameEntity getTagName(String name);

	/**
	 * Finds a tag by key.
	 * @param key Tag key.
	 * @return The requested tag or {@code null} if it is not found.
	 */
	TagEntity getTag(TagKey key);

	/**
	 * Load applied tags.
	 * @return Every applied tag grouped by set and name.
	 */
	List<TagEntity> loadAppliedTags();

	/**
	 * Load all created tags for a set.
	 * @param set Set name.
	 * @return Every created tag for the provided set.
	 */
	List<TagEntity> loadTagsBySetName(String set);

	/**
	 * Find comments.
	 * @param <D> Output type.
	 * @param filter Search filter.
	 * @param transformer Transformation function.
	 * @return A page of results.
	 */
	<D> PageDTO<D> findComments(CommentInternalFilterDTO filter,
		Function<? super CommentEntity, ? extends D> transformer);

	/**
	 * Load comments.
	 * @return Every non-deleted comment.
	 */
	List<Object[]> loadComments();

	/**
	 * Load comments by time.
	 * @param timestamp Starting timestamp.
	 * @return Every non-deleted comment updated after the specified timestamp.
	 */
	List<Object[]> loadComments(long timestamp);
	
	/**
	 * Gets the next value of a sequence. The row is locked in the current transaction.
	 * @param id Sequence name.
	 * @return The next value of the specified sequence.
	 */
	long getNextValue(final String id) throws SequenceNotFoundException;

	/**
	 * 
	 * @param <D>
	 * @param filter
	 * @param transformer
	 * @return
	 */
	<D> List<D> findBatch(LogTableFilterDTO filter, Function<? super LogTableEntity, ? extends D> transformer);

	/**
	 * 
	 * @param <D>
	 * @param filter
	 * @param transformer
	 * @return
	 */
	<D> PageDTO<D> findNotices(NoticeFilterDTO filter, Function<? super NoticeEntity, ? extends D> transformer);

	/**
	 * Search favorites for a member on a community.
	 * @param <D> Return type.
	 * @param filter The filter.
	 * @param transformer The transformer.
	 * @return The favorites.
	 */
	<D> PageDTO<D> findFavorites(FavoriteFilterDTO filter, Function<? super FavoriteEntity, ? extends D> transformer);

	/**
	 * 
	 * @param <D>
	 * @param filter
	 * @param transformer
	 * @return
	 */
	<D> PageDTO<D> findRecommendations(RecommendationFilterDTO filter,
		Function<? super RecommendationEntity, ? extends D> transformer);

	/**
	 * Load the counter validated members of all communities.
	 * @return A map from community id to validated member count.
	 */
	Map<UUID, Integer> getValidatedMembers();

	/**
	 * Load the counter non-validated members of all communities.
	 * @return A map from community id to non-validated member count.
	 */
	Map<UUID, Integer> getNonValidatedMembers();

	/**
	 * Returns ratings events for a rating and a source.
	 * @param filter The filter.
	 * @return The source ratings events.
	 */
	List<RatingEventEntity> findRatingEvents(RatingFilterDTO filter);

	CommunityEntity findCommunityByLastCode(String lastCode);

	MemberEntity findMemberByLastCode(String lastCode);

	<D> PageDTO<D> findMembersWithDeleted(PaginationDTO pagination,
		Function<? super MemberEntity, ? extends D> transformer);

	List<Object[]> findMembersNoGlobal();

	/**
	 * Returns resources with unmoderated comments.
	 * @param communityId Community Id.
	 * @param max Maximum number of results.
	 * @return Array of resource id, community id, number of unmoderated comments.
	 * @throws ServiceException
	 */
	List<Object[]> getRWUC(int max) throws ServiceException;

	/**
	 * Returns resources with unmoderated comments (RWUC) in a community.
	 * @param communityId Community Id.
	 * @param max Maximum number of results.
	 * @return Array of resource id, unmoderated comments.
	 * @throws ServiceException
	 */
	List<Object[]> getRWUCInCommunity(UUID communityId, int max) throws ServiceException;

	/**
	 * 
	 * @param commentId
	 * @param sourceId
	 * @return
	 * @throws ServiceException
	 */
	int countRatesByCommentAndSource(Long commentId, Long sourceId) throws ServiceException;

	int numberOfComments(Long relationshipId) throws ServiceException;

}
