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

package cascading.operation.aggregator;

import java.beans.ConstructorProperties;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import cascading.flow.FlowProcess;
import cascading.operation.Aggregator;
import cascading.operation.AggregatorCall;
import cascading.operation.BaseOperation;
import cascading.tuple.Fields;
import cascading.tuple.Tuple;
import cascading.tuple.TupleEntry;

/** Class ExtentBase is the base class for First and Last. */
public abstract class ExtentBase extends BaseOperation<Tuple[]> implements Aggregator<Tuple[]>
  {
  /** Field ignoreTuples */
  private final Collection<Tuple> ignoreTuples;

  @ConstructorProperties({"fieldDeclaration"})
  protected ExtentBase( Fields fieldDeclaration )
    {
    super( fieldDeclaration );
    this.ignoreTuples = null;
    }

  @ConstructorProperties({"numArgs", "fieldDeclaration"})
  protected ExtentBase( int numArgs, Fields fieldDeclaration )
    {
    super( numArgs, fieldDeclaration );
    ignoreTuples = null;
    }

  @ConstructorProperties({"fieldDeclaration", "ignoreTuples"})
  protected ExtentBase( Fields fieldDeclaration, Tuple... ignoreTuples )
    {
    super( fieldDeclaration );
    this.ignoreTuples = new HashSet<Tuple>();
    Collections.addAll( this.ignoreTuples, ignoreTuples );
    }

  @SuppressWarnings("unchecked")
  public void start( FlowProcess flowProcess, AggregatorCall<Tuple[]> aggregatorCall )
    {
    if( aggregatorCall.getContext() == null )
      aggregatorCall.setContext( new Tuple[1] );
    else
      aggregatorCall.getContext()[ 0 ] = null;
    }

  public void aggregate( FlowProcess flowProcess, AggregatorCall<Tuple[]> aggregatorCall )
    {
    if( ignoreTuples != null && ignoreTuples.contains( aggregatorCall.getArguments().getTuple() ) )
      return;

    performOperation( aggregatorCall.getContext(), aggregatorCall.getArguments() );
    }

  protected abstract void performOperation( Tuple[] context, TupleEntry entry );

  public void complete( FlowProcess flowProcess, AggregatorCall<Tuple[]> aggregatorCall )
    {
    if( aggregatorCall.getContext()[ 0 ] != null )
      aggregatorCall.getOutputCollector().add( getResult( aggregatorCall ) );
    }

  protected Tuple getResult( AggregatorCall<Tuple[]> aggregatorCall )
    {
    return aggregatorCall.getContext()[ 0 ];
    }

  @Override
  public boolean equals( Object object )
    {
    if( this == object )
      return true;
    if( !( object instanceof ExtentBase ) )
      return false;
    if( !super.equals( object ) )
      return false;

    ExtentBase that = (ExtentBase) object;

    if( ignoreTuples != null ? !ignoreTuples.equals( that.ignoreTuples ) : that.ignoreTuples != null )
      return false;

    return true;
    }

  @Override
  public int hashCode()
    {
    int result = super.hashCode();
    result = 31 * result + ( ignoreTuples != null ? ignoreTuples.hashCode() : 0 );
    return result;
    }
  }
