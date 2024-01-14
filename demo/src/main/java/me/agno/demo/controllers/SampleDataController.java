package me.agno.demo.controllers;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.function.Consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.agno.demo.DemoApplication;
import me.agno.demo.model.Order;
import me.agno.gridcore.IGridColumnCollection;
import me.agno.gridcore.server.GridServer;
import me.agno.gridcore.server.IGridServer;
import me.agno.gridcore.utils.ItemsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/sampledata")
public class SampleDataController {

	@Autowired
	EntityManagerFactory entityManagerFactory;

	@GetMapping(value = "getordersgrid", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ItemsDTO<Order>> getUser(HttpServletRequest request) throws JsonProcessingException {

		EntityManager em = entityManagerFactory.createEntityManager();

		Consumer<IGridColumnCollection<Order>> columns = c ->
		{
			c.add("orderID",Integer.class);
			c.add("orderDate", Instant.class);
			//c.add("customer.companyName", String.class);
			//c.add("customer.contactName", String.class);
			//c.add("customer.country", String.class, true);
			c.add("freight", BigDecimal.class);
			//c.add("customer.isVip",Boolean.class);
		};

		IGridServer<Order> server = new GridServer<Order>(em, Order.class, null, null,
				request.getParameterMap(), columns)
				.withPaging(10)
				.sortable()
				.filterable();

		var items = server.getItemsToDisplay();
		return ResponseEntity.ok(items);
		//var result = DemoApplication.mapper.writeValueAsString(items);
		//return items;
	}
}
