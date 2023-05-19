package com.azad.basicecommerce.common.generics;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;

/***
 * A generic rest controller interface to resolve CRUD API requests.
 * Implementing classes must follow the principle of HATEOAS.
 *
 * @param <Q> Entity Request
 * @param <S> Entity Response
 */
public interface GenericApiRestController<Q, S> {

    ResponseEntity<EntityModel<S>> createEntity(Q request);
    ResponseEntity<CollectionModel<EntityModel<S>>> getAllEntities(int page, int limit, String sort, String order);
    ResponseEntity<EntityModel<S>> getEntity(Long id);
    ResponseEntity<EntityModel<S>> getEntity(String uid);
    ResponseEntity<EntityModel<S>> updateEntity(Long id, Q updatedRequest);
    ResponseEntity<EntityModel<S>> updateEntity(String uid, Q updatedRequest);
    ResponseEntity<?> deleteEntity(Long id);
    ResponseEntity<?> deleteEntity(String uid);
}
