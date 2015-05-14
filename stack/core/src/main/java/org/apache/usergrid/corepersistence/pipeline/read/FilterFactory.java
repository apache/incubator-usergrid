/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.usergrid.corepersistence.pipeline.read;


import org.apache.usergrid.corepersistence.pipeline.read.collect.EntityFilter;
import org.apache.usergrid.corepersistence.pipeline.read.collect.IdCursorSerializer;
import org.apache.usergrid.corepersistence.pipeline.read.elasticsearch.CandidateEntityFilter;
import org.apache.usergrid.corepersistence.pipeline.read.elasticsearch.CandidateIdFilter;
import org.apache.usergrid.corepersistence.pipeline.read.elasticsearch.ElasticSearchCollectionFilter;
import org.apache.usergrid.corepersistence.pipeline.read.elasticsearch.ElasticSearchConnectionFilter;
import org.apache.usergrid.corepersistence.pipeline.read.graph.EntityIdFilter;
import org.apache.usergrid.corepersistence.pipeline.read.graph.EntityLoadFilter;
import org.apache.usergrid.corepersistence.pipeline.read.graph.ReadGraphCollectionByIdFilter;
import org.apache.usergrid.corepersistence.pipeline.read.graph.ReadGraphCollectionFilter;
import org.apache.usergrid.corepersistence.pipeline.read.graph.ReadGraphConnectionByIdFilter;
import org.apache.usergrid.corepersistence.pipeline.read.graph.ReadGraphConnectionByTypeFilter;
import org.apache.usergrid.corepersistence.pipeline.read.graph.ReadGraphConnectionFilter;
import org.apache.usergrid.persistence.model.entity.Id;

import com.google.common.base.Optional;
import com.google.inject.assistedinject.Assisted;


/**
 * A factory for generating read commands
 */
public interface FilterFactory {


    /**
     * Generate a new instance of the command with the specified parameters
     *
     * @param collectionName The collection name to use when reading the graph
     */
    ReadGraphCollectionFilter readGraphCollectionFilter( final String collectionName );

    /**
     * Read a connection between two entities, the incoming and the target entity
     *
     * @param collectionName The collection name to use when reading the edge
     * @param targetId The target id to use when traversing the graph
     */
    ReadGraphCollectionByIdFilter readGraphCollectionByIdFilter( final String collectionName, final Id targetId );

    /**
     * Generate a new instance of the command with the specified parameters
     *
     * @param connectionName The connection name to use when traversing the graph
     */
    ReadGraphConnectionFilter readGraphConnectionFilter( final String connectionName );

    /**
     * Generate a new instance of the command with the specified parameters
     *
     * @param connectionName The connection name to use when traversing the graph
     * @param entityType The entity type to use when traversing the graph
     */
    ReadGraphConnectionByTypeFilter readGraphConnectionByTypeFilter(
        @Assisted( "connectionName" ) final String connectionName, @Assisted( "entityType" ) final String entityType );


    /**
     * Read a connection directly between two identifiers
     *
     * @param connectionName The connection name to use when traversing the graph
     * @param targetId The target Id to use when traversing the graph
     */
    ReadGraphConnectionByIdFilter readGraphConnectionByIdFilter( final String connectionName, final Id targetId );

    /**
     * Generate a new instance of the command with the specified parameters
     *
     * @param query The query to use when querying the entities in the collection
     * @param collectionName The collection name to use when querying
     */
    ElasticSearchCollectionFilter elasticSearchCollectionFilter( @Assisted( "query" ) final String query,
                                                                 @Assisted( "collectionName" )
                                                                 final String collectionName,
                                                                 @Assisted( "entityType" ) final String entityType );


    /**
     * Generate a new instance of the command with the specified parameters
     *
     * @param query The query to use when querying the entities in the connection
     * @param connectionName The type of connection to query
     * @param connectedEntityType The type of entity in the connection.  Leave absent to query all entity types
     */
    ElasticSearchConnectionFilter elasticSearchConnectionFilter( @Assisted( "query" ) final String query,
                                                                 @Assisted( "connectionName" )
                                                                 final String connectionName,
                                                                 @Assisted( "connectedEntityType" )
                                                                 final Optional<String> connectedEntityType );


    /**
     * Generate a new instance of the command with the specified parameters
     */
    EntityLoadFilter entityLoadFilter();

    /**
     * Get the collector for collection candidate results to entities
     */
    CandidateEntityFilter candidateEntityFilter();

    /**
     * Get a candidate ids verifier for collection results.  Should be inserted into pipelines where a query filter is
     * an intermediate step, not a final filter before collectors
     */
    CandidateIdFilter candidateResultsIdVerifyFilter();

    /**
     * Get an entity id filter.  Used as a 1.0->2.0 bridge since we're not doing full traversals
     *
     * @param entityId The entity id to emit
     */
    EntityIdFilter getEntityIdFilter( final Id entityId );


    /**
     * Create a new instance of our entity filter
     * @return
     */
    EntityFilter entityFilter();
}