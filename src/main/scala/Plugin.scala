import javax.servlet.ServletContext

import gitbucket.core.plugin.PluginRegistry
import gitbucket.core.service.SystemSettingsService.SystemSettings
import gitbucket.ipynb.IpynbRenderer
import io.github.gitbucket.solidbase.model.Version

import scala.util.Try

class Plugin extends gitbucket.core.plugin.Plugin {
  override val pluginId: String = "ipynb"
  override val pluginName: String = "ipynb Plugin"
  override val description: String = "Provides ipynb rendering for GitBucket."
  override val versions: List[Version] = List(
    new Version("1.0.0")
  )

  private[this] var renderer: Option[IpynbRenderer] = None

  override def initialize(registry: PluginRegistry, context: ServletContext, settings: SystemSettings): Unit = {
    val test = Try{ new IpynbRenderer() }
    val ipynb = test.get
    registry.addRenderer("ipynb", ipynb)
    renderer = Option(ipynb)
    super.initialize(registry, context, settings)
  }

  override def shutdown(registry: PluginRegistry, context: ServletContext, settings: SystemSettings): Unit = {
    renderer.map(r => r.shutdown())
  }

  override val assetsMappings = Seq("/ipynb" -> "/ipynb/assets")
}