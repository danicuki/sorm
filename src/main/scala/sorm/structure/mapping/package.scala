package sorm.structure

import sorm._
import reflection._
import ddl._
import structure._
import extensions.Extensions._

package object mapping {

  def columnsForContainerTable
    ( m : Mapping ) 
    : Iterable[Column]
    = m match {
        case m : CollectionMapping ⇒
          Nil
        case m : TableMapping ⇒ 
          m.primaryKeyColumns
            .view
            .map{ c ⇒ 
              c.copy(
                autoIncrement
                  = false,
                name
                  = m.columnName + "$" + c.name,
                nullable
                  = m.membership.isInstanceOf[Some[Membership.EntityProperty]]
              )
            }
        case m : HasChildren ⇒ 
          m.children.view flatMap columnsForContainerTable
        case m : ValueMapping ⇒
          m.column :: Nil
      }


}