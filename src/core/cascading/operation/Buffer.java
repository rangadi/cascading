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

package cascading.operation;

import cascading.flow.FlowProcess;

/**
 * A Buffer is similar to an {@link Aggregator} by the fact that it operates on unique groups of values. It differs
 * by the fact that an {@link java.util.Iterator} is provided and it is the responsibility
 * of the {@link #operate(cascading.flow.FlowProcess, BufferCall)} method to iterate overall all the input
 * arguments returned by this Iterator, if any.
 * <p/>
 * For the case where a Buffer follows a CoGroup, the method {@link #operate(cascading.flow.FlowProcess, BufferCall)}
 * will be called for every unique group whether or not there are values available to iterate over. This may be
 * counter-intuitive for the case of an 'inner join' where the left or right stream may have a null grouping key value.
 * Regardless, the current grouping value can be retrieved through {@link BufferCall#getGroup()}.
 * <p/>
 * Buffer is very useful when header or footer values need to be inserted into a grouping, or if values need to be
 * inserted into the middle of the group values. For example, consider a stream of timestamps. A Buffer could
 * be used to add missing entries, or to calculate running or moving averages over a smaller "window" within the grouping.
 * <p/>
 * There may be only one Buffer after a {@link cascading.pipe.GroupBy} or {@link cascading.pipe.CoGroup}. And there
 * may not be any additional {@link cascading.pipe.Every} pipes before or after the buffers Every pipe instance. A
 * {@link cascading.flow.PlannerException} will be thrown if these rules are violated.
 * <p/>
 * Buffer implementations should be re-entrant. There is no guarantee a Buffer instance will be executed in a
 * unique vm, or by a single thread. Also, note the Iterator will return the same {@link cascading.tuple.TupleEntry}
 * instance, but with new values in its child {@link cascading.tuple.Tuple}.
 */
public interface Buffer<C> extends Operation<C>
  {
  /**
   * Method operate is called once for each grouping. {@link BufferCall} passes in an {@link java.util.Iterator}
   * that returns an argument {@link cascading.tuple.TupleEntry} for each value in the grouping defined by the
   * argument selector on the parent Every pipe instance.
   * <p/>
   * TupleEntry entry, or entry.getTuple() should not be stored directly in a collection or modified.
   * A copy of the tuple should be made via the {@code new Tuple( entry.getTuple() )} copy constructor.
   * <p/>
   * This method is called for every unique group, whether or not there are values in the arguments Iterator.
   *
   * @param flowProcess of type FlowProcess
   * @param bufferCall  of type BufferCall
   */
  void operate( FlowProcess flowProcess, BufferCall<C> bufferCall );
  }