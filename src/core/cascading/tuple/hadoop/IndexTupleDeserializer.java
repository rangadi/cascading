/*
 * Copyright (c) 2007-2010 Concurrent, Inc. All Rights Reserved.
 *
 * Project and contact information: http://www.cascading.org/
 *
 * This file is part of the Cascading project.
 *
 * Cascading is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Cascading is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Cascading.  If not, see <http://www.gnu.org/licenses/>.
 */

package cascading.tuple.hadoop;

import java.io.IOException;

import cascading.tuple.IndexTuple;

class IndexTupleDeserializer extends BaseDeserializer<IndexTuple>
  {
  IndexTupleDeserializer( SerializationElementReader elementReader )
    {
    super( elementReader );
    }

  public IndexTuple deserialize( IndexTuple indexTuple ) throws IOException
    {
    if( indexTuple == null )
      return inputStream.readIndexTuple();

    return inputStream.readIndexTuple( indexTuple );
    }
  }