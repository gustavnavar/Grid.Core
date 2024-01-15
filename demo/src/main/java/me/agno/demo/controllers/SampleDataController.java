package me.agno.demo.controllers;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.Instant;
import java.util.function.Consumer;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.http.HttpServletRequest;
import me.agno.demo.model.Order;
import me.agno.gridcore.IGridColumnCollection;
import me.agno.gridcore.server.GridServer;
import me.agno.gridcore.server.IGridServer;
import me.agno.gridcore.utils.ItemsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = {"/api/sampledata", "/api/SampleData"})
public class SampleDataController {

	@Autowired
	EntityManagerFactory entityManagerFactory;

	@GetMapping(value = {"getordersgrid", "GetOrdersGrid"}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ItemsDTO<Order>> getOrdersGrid(HttpServletRequest request) {

		EntityManager em = entityManagerFactory.createEntityManager();

		Consumer<IGridColumnCollection<Order>> columns = c ->
		{
			c.add("orderID",Integer.class);
			c.add("orderDate", Instant.class, "orderCustomDate");
			c.add("customer.companyName", String.class);
			c.add("customer.contactName", String.class);
			c.add("customer.country", String.class, true);
			c.add("freight", BigDecimal.class);
			c.add("customer.isVip",Boolean.class);
		};

		IGridServer<Order> server = new GridServer<Order>(em, Order.class, null, null,
				request.getParameterMap(), columns)
				.withPaging(10)
				.sortable()
				.filterable();

		var items = server.getItemsToDisplay();
		return ResponseEntity.ok(items);
	}

	@GetMapping(value = {"getordersgridwithtotals", "GetOrdersGridWithTotals"}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ItemsDTO<Order>> getOrdersGridWithTotals(HttpServletRequest request) {

		EntityManager em = entityManagerFactory.createEntityManager();

		Consumer<IGridColumnCollection<Order>> columns = c ->
		{
			c.add("orderID",Integer.class).sum(true);
			c.add("orderDate", Instant.class, "orderCustomDate").max(true).min(true);
			c.add("customer.companyName", String.class).max(true).min(true);
			c.add("customer.contactName", String.class).max(true).min(true);
			c.add("freight", BigDecimal.class).sum(true).average(true).max(true).min(true)
					.calculate("Average 2", x -> x.getGrid().getItemsCount() == 0
							||  x.get("freight") == null || x.get("freight").getSumValue().getNumber().isEmpty()
							? "" : x.get("freight").getSumValue().getNumber().get()
								.divide(BigDecimal.valueOf(x.getGrid().getItemsCount()), MathContext.DECIMAL32))
					.calculate("Average 3", x -> x.get("orderID") == null || x.get("orderID").getSumValue() == null
							|| x.get("orderID").getSumValue().getNumber().isEmpty()
							|| x.get("orderID").getSumValue().getNumber().get() == BigDecimal.ZERO
							|| x.get("freight") == null || x.get("freight").getSumValue() == null
							|| x.get("freight").getSumValue().getNumber().isEmpty()
							? "" : x.get("freight").getSumValue().getNumber().get()
								.divide(x.get("orderID").getSumValue().getNumber().get(),MathContext.DECIMAL32));
			c.add("customer.isVip",Boolean.class);
		};

		IGridServer<Order> server = new GridServer<Order>(em, Order.class, null, null,
				request.getParameterMap(), columns)
				.withPaging(10)
				.sortable()
				.filterable();

		var items = server.getItemsToDisplay();
		return ResponseEntity.ok(items);
	}
}