/*
 * Copyright (c) 2002-2017 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.neo4j.kernel.impl.store;

import org.neo4j.io.IOUtils;
import org.neo4j.kernel.impl.store.record.AbstractBaseRecord;
import org.neo4j.kernel.impl.store.record.RelationshipGroupRecord;
import org.neo4j.kernel.impl.store.record.RelationshipRecord;

import static org.neo4j.kernel.impl.store.record.RecordLoad.NORMAL;

/**
 * Container for {@link RecordCursor}s for different stores. Intended to be reused by pooled transactions.
 */
public class RecordCursors implements AutoCloseable
{
    private final RecordCursor<RelationshipGroupRecord> relationshipGroup;

    public RecordCursors( NeoStores neoStores )
    {
        relationshipGroup = newCursor( neoStores.getRelationshipGroupStore() );
    }

    private static <R extends AbstractBaseRecord> RecordCursor<R> newCursor( RecordStore<R> store )
    {
        return store.newRecordCursor( store.newRecord() ).acquire( store.getNumberOfReservedLowIds(), NORMAL );
    }

    @Override
    public void close()
    {
        IOUtils.closeAll( RuntimeException.class, relationshipGroup );
    }

    public RecordCursor<RelationshipGroupRecord> relationshipGroup()
    {
        return relationshipGroup;
    }
}
