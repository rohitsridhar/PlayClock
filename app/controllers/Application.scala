package controllers

import play.api._
import play.api.mvc._
import play.api.libs.iteratee._
import play.api.libs.concurrent._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.{ Comet }

import scala.concurrent.duration._

object Application extends Controller {

  lazy val clock: Enumerator[String] = {

    import java.util._
    import java.text._

    val dateFormat = new SimpleDateFormat("HH mm ss")

    Enumerator.fromCallback1 { TRUE =>
      Promise.timeout(Some(dateFormat.format(new Date)), 100 milliseconds)
    }
  }

 /* def index = Action {
    Ok(views.html.index("Your new application is not yet ready."))
    //Ok(views.html.main("Hi"))
  } */

  def index = Action {
    Ok(views.html.index())
  }

  def liveClock = Action {
    Ok.stream(clock &> Comet(callback = "parent.clockChanged"))
  }

}