package controllers

import java.text.SimpleDateFormat
import java.util.Date

import play.api._
import play.api.mvc._
import play.api.libs.iteratee._
import play.api.libs.concurrent._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.Comet

import scala.concurrent.duration._

object Application extends Controller {

  lazy val clock: Enumerator[String] = {

    import java.util._
    import java.text._

    val dateFormat = new SimpleDateFormat("HH : mm : ss zz")

    Enumerator.fromCallback1 { TRUE =>
      Promise.timeout(Some(dateFormat.format(new Date)), 100 milliseconds)
    }
  }

  def index = Action {
    val dateFormat = new SimpleDateFormat("EEE, MMM d, yyyy")
    Ok(views.html.index(dateFormat.format(new Date)))
  }

  def liveClock = Action {
    Ok.chunked(clock &> Comet(callback = "parent.clockChanged"))
  }

}