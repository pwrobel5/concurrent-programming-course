package webcrawler

import java.io.FileOutputStream

import org.htmlcleaner.TagNode

object WebCrawler extends App {
  import scala.concurrent.Future
  import scala.concurrent.ExecutionContext.Implicits.global
  import scala.language.postfixOps
  import scala.util.{Success, Failure}
  import java.io.FileWriter

  import scala.io.Source
  import org.htmlcleaner.HtmlCleaner
  import java.net.URL

  def saveFileFromUrl(url: String, outputName: String = "index.html"): Future[Any] = Future {
    try {
      val html = Source.fromURL(url)
      val urlObj = new URL(url)
      val filePath = urlObj.getPath
      var fileName = filePath.substring(filePath.lastIndexOf('/') + 1)

      if(fileName == "") {
        fileName = outputName
      }

      fileName = "./downloads/" + fileName

      val output = new FileWriter(fileName)
      output.write(html.mkString)
      output.close()
    } catch {
      case e: Exception => println("Error with saving page file from " + url)
    }
  }

  def saveImagesFromHTML(rootNode: TagNode, rootUrl: String, outputDefaultName: String = "image.png"): Future[Any] = Future {
    val images = rootNode.getElementsByName("img", true)
    images map { image =>
      var imageUrl = image.getAttributeByName("src")
      if(!imageUrl.contains("://") && !imageUrl.contains("www.")) {
        imageUrl = rootUrl + imageUrl
      }

      try {
        val imageUrlObject = new URL(imageUrl)
        val input = imageUrlObject.openStream()
        val filePath = imageUrlObject.getPath
        var fileName = filePath.substring(filePath.lastIndexOf('/') + 1)
        if (fileName == "") {
          fileName = outputDefaultName
        }

        fileName = "./downloads/" + fileName
        val output = new FileOutputStream(fileName)
        val byteArray = Stream.continually(input.read).takeWhile(-1 !=).map(_.toByte).toArray
        output.write(byteArray)
        output.close()
        input.close()
        println("Image from " + imageUrl + " successfully saved")
      } catch {
        case e: Exception => println("Error with saving image from " + imageUrl)
      }
    }
  }

  def crawl(url: String, depth: Int): Future[Any] = Future {
    if(depth > 0) {
      val savingFile = saveFileFromUrl(url)

      savingFile onComplete {
        case Success(_) => println("File from " + url + " successfully saved")
        case Failure(e) => e.printStackTrace()
      }

      val rootNode = cleaner.clean(new URL(url))
      val imgSaver = saveImagesFromHTML(rootNode, url)

      imgSaver onComplete {
        case Success(_) => println("Images from " + url + " successfully saved")
        case Failure(e) => e.printStackTrace()
      }

      val elements = rootNode.getElementsByName("a", true)
      elements map { elem =>
        var extractedUrl = elem.getAttributeByName("href")
        if(!extractedUrl.contains("://") && !extractedUrl.contains("www.")) {
          extractedUrl = url + extractedUrl
        }
        crawl(extractedUrl, depth - 1)
      }
    }
  }

  val url = "http://google.com/"
  val depth = 3

  val cleaner = new HtmlCleaner
  val props = cleaner.getProperties

  val result = crawl(url, depth)

  result onComplete {
    case Success(_) => println("Success")
    case Failure(e) => e.printStackTrace()
  }

  Thread.sleep(120000)
}
