package cta;

import java.awt.EventQueue;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class TraceEngineApplication {

	public static void main(String[] args) {
		var ctx = new SpringApplicationBuilder(TraceEngineApplication.class).web(WebApplicationType.SERVLET)
				.headless(false).run(args);

		EventQueue.invokeLater(() -> {
			var vis = ctx.getBean(Visualizador.class);
			vis.reloadCatalogos();
			//vis.setVisible(true);
		});

	}

}
