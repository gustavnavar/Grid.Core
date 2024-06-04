package me.agno.demo.controllers;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Collection;
import java.util.Random;
import java.util.function.Consumer;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import me.agno.demo.model.*;
import me.agno.demo.repositories.*;
import me.agno.gridjavacore.IGridColumnCollection;
import me.agno.gridjavacore.SelectItem;
import me.agno.gridjavacore.columns.GridCoreColumn;
import me.agno.gridjavacore.server.GridServer;
import me.agno.gridjavacore.server.IGridServer;
import me.agno.gridjavacore.utils.ItemsDTO;
import me.agno.gridjavacore.utils.Pair;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.hibernate.query.sqm.tree.SqmCopyContext;
import org.hibernate.query.sqm.tree.select.SqmSelectStatement;
import org.hibernate.query.sqm.tree.select.SqmSubQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = {"/api/sampledata", "/api/SampleData"})
public class SampleDataController {

	private final OrderRepository orderRepository;
	private final CustomerRepository customerRepository;
	private final EmployeeRepository employeeRepository;
	private final ProductRepository productRepository;
	private final ShipperRepository shipperRepository;

	@Autowired
	EntityManagerFactory entityManagerFactory;

	@GetMapping(value = {"getordersgrid", "GetOrdersGrid"}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ItemsDTO<Order>> getOrdersGrid(HttpServletRequest request) {

		EntityManager em = entityManagerFactory.createEntityManager();

		Consumer<IGridColumnCollection<Order>> columns = c -> {
			c.add("orderID", Integer.class);
			c.add("orderDate", LocalDateTime.class, "orderCustomDate");
			c.add("customer.companyName", String.class);
			c.add("customer.contactName", String.class);
			c.add("customer.country", String.class, true);
			c.add("freight", BigDecimal.class);
			c.add("customer.isVip",Boolean.class);
		};

		IGridServer<Order> server = new GridServer<>(em, Order.class, request.getParameterMap(), columns)
				.withPaging(10)
				.sortable()
				.filterable()
				.setRemoveDiacritics("dbo.RemoveDiacritics");

		var items = server.getItemsToDisplay();
		return ResponseEntity.ok(items);
	}

	@GetMapping(value = {"getordersgridordersautogeneratecolumns", "GetOrdersGridordersAutoGenerateColumns"}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ItemsDTO<Order>> getOrdersGridordersAutoGenerateColumns(HttpServletRequest request) {

		EntityManager em = entityManagerFactory.createEntityManager();

		IGridServer<Order> server = new GridServer<>(em, Order.class, request.getParameterMap(), null)
				.autoGenerateColumns()
				.sortable()
				.filterable()
				.setRemoveDiacritics("dbo.RemoveDiacritics");

		var items = server.getItemsToDisplay();
		return ResponseEntity.ok(items);
	}

	@GetMapping(value = {"getordersgridwithtotals", "GetOrdersGridWithTotals"}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ItemsDTO<Order>> getOrdersGridWithTotals(HttpServletRequest request) {

		EntityManager em = entityManagerFactory.createEntityManager();

		Consumer<IGridColumnCollection<Order>> columns = c -> {
			c.add("orderID", Integer.class).sum(true);
			c.add("orderDate", LocalDateTime.class, "orderCustomDate").max(true).min(true);
			c.add("customer.companyName", String.class).max(true).min(true);
			c.add("customer.contactName", String.class).max(true).min(true);
			c.add("freight", BigDecimal.class).sum(true).average(true).max(true).min(true)
					.calculate("Average 2", x -> x.getGrid().getItemsCount() == 0
							||  x.get("freight") == null || x.get("freight").getSumValue().getNumber().isEmpty()
							? "" : x.get("freight").getSumValue().getNumber().get()
								.divide(BigDecimal.valueOf(x.getGrid().getItemsCount()), MathContext.DECIMAL32))
					.calculate("Average 3", x -> x.get("orderID") == null || x.get("orderID").getSumValue() == null
							|| x.get("orderID").getSumValue().getNumber().isEmpty()
							|| x.get("orderID").getSumValue().getNumber().get().equals(BigDecimal.ZERO)
							|| x.get("freight") == null || x.get("freight").getSumValue() == null
							|| x.get("freight").getSumValue().getNumber().isEmpty()
							? "" : x.get("freight").getSumValue().getNumber().get()
								.divide(x.get("orderID").getSumValue().getNumber().get(),MathContext.DECIMAL32));
			c.add("customer.isVip",Boolean.class);
		};

		IGridServer<Order> server = new GridServer<>(em, Order.class, request.getParameterMap(), columns)
				.withPaging(10)
				.sortable()
				.filterable()
				.setRemoveDiacritics("dbo.RemoveDiacritics");

		var items = server.getItemsToDisplay();
		return ResponseEntity.ok(items);
	}

	@GetMapping(value = {"getordersgridsearchable", "GetOrdersGridSearchable"}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ItemsDTO<Order>> getOrdersGridSearchable(HttpServletRequest request) {

		EntityManager em = entityManagerFactory.createEntityManager();

		Consumer<IGridColumnCollection<Order>> columns = c -> {
			c.add("orderID", Integer.class);
			c.add("orderDate", LocalDateTime.class, "orderCustomDate");
			c.add("customer.companyName", String.class);
			c.add("customer.contactName", String.class);
			c.add("customer.country", String.class, true);
			c.add("freight", BigDecimal.class);
			c.add("customer.isVip",Boolean.class);
		};

		IGridServer<Order> server = new GridServer<>(em, Order.class, request.getParameterMap(), columns)
				.withPaging(10)
				.sortable()
				.filterable()
				.searchable(true, false, false)
				.setRemoveDiacritics("dbo.RemoveDiacritics");

		var items = server.getItemsToDisplay();
		return ResponseEntity.ok(items);
	}

	@GetMapping(value = {"getordersgridEextsorting", "GetOrdersGridExtSorting"}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ItemsDTO<Order>> getOrdersGridExtSorting(HttpServletRequest request) {

		EntityManager em = entityManagerFactory.createEntityManager();

		Consumer<IGridColumnCollection<Order>> columns = c -> {
			var shipVia = c.createColumn("shipVia", String.class, false, null);
			var freight = c.createColumn("freight", BigDecimal.class, false, null);

			c.add("orderID", Integer.class);
			c.add("orderDate", LocalDateTime.class, "orderCustomDate");
			c.add("customer.companyName", String.class)
					.thenSortBy((GridCoreColumn<Order, String>)shipVia)
					.thenSortByDescending((GridCoreColumn<Order, BigDecimal>)freight);
			c.add("customer.contactName", String.class);
			c.add(shipVia);
			c.add(freight);
			c.add("customer.isVip",Boolean.class);
		};

		IGridServer<Order> server = new GridServer<>(em, Order.class, request.getParameterMap(), columns)
				.withPaging(10)
				.sortable()
				.filterable()
				.setRemoveDiacritics("dbo.RemoveDiacritics");

		var items = server.getItemsToDisplay();
		return ResponseEntity.ok(items);
	}

	@GetMapping(value = {"getordersgridgroupable", "GetOrdersGridGroupable"}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ItemsDTO<Order>> getOrdersGridGroupable(HttpServletRequest request) {

		EntityManager em = entityManagerFactory.createEntityManager();

		Consumer<IGridColumnCollection<Order>> columns = c -> {
			var shipVia = c.createColumn("shipVia", String.class, false, null);
			var freight = c.createColumn("freight", BigDecimal.class, false, null);

			c.add("orderID", Integer.class);
			c.add("orderDate", LocalDateTime.class, "orderCustomDate");
			c.add("customer.companyName", String.class)
					.thenSortBy((GridCoreColumn<Order, String>)shipVia)
					.thenSortByDescending((GridCoreColumn<Order, BigDecimal>)freight);
			c.add("customer.contactName", String.class);
			c.add(shipVia);
			c.add(freight);
			c.add("customer.isVip",Boolean.class);
		};

		IGridServer<Order> server = new GridServer<>(em, Order.class, request.getParameterMap(), columns)
				.withPaging(10)
				.sortable()
				.filterable()
				.setRemoveDiacritics("dbo.RemoveDiacritics");

		var items = server.getItemsToDisplay();
		return ResponseEntity.ok(items);
	}

	@GetMapping(value = {"getordersgridwithsubgrids", "GetOrdersGridWithSubgrids"}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ItemsDTO<Order>> getOrdersGridWithSubgrids(HttpServletRequest request) {

		EntityManager em = entityManagerFactory.createEntityManager();

		Consumer<IGridColumnCollection<Order>> columns = c -> {
			var shipVia = c.createColumn("shipVia", String.class, false, null);
			var freight = c.createColumn("freight", BigDecimal.class, false, null);

			c.add("orderID", Integer.class);
			c.add("orderDate", LocalDateTime.class, "orderCustomDate");
			c.add("customer.companyName", String.class)
					.thenSortBy((GridCoreColumn<Order, String>)shipVia)
					.thenSortByDescending((GridCoreColumn<Order, BigDecimal>)freight);
			c.add("customer.contactName", String.class);
			c.add(shipVia);
			c.add(freight);
			c.add("customer.isVip",Boolean.class);
		};

		IGridServer<Order> server = new GridServer<Order>(em, Order.class, request.getParameterMap(), columns)
				.withPaging(10)
				.sortable()
				.filterable()
				.setRemoveDiacritics("dbo.RemoveDiacritics");

		var items = server.getItemsToDisplay();
		return ResponseEntity.ok(items);
	}

	@GetMapping(value = {"ordercolumnslistfilter", "OrderColumnsListFilter"}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ItemsDTO<Order>> orderColumnsListFilter(HttpServletRequest request) {

		EntityManager em = entityManagerFactory.createEntityManager();

		Consumer<IGridColumnCollection<Order>> columns = c -> {
			c.add("orderID", Integer.class);
			c.add("orderDate", LocalDateTime.class, "orderCustomDate");
			c.add("customer.companyName", String.class);
			c.add("customer.contactName", String.class);
			c.add("customer.country", String.class, true);
			c.add("shipVia", String.class);
			c.add("freight", BigDecimal.class);
			c.add("customer.isVip",Boolean.class);
		};

		IGridServer<Order> server = new GridServer<>(em, Order.class, request.getParameterMap(), columns)
				.withPaging(10)
				.sortable()
				.filterable()
				.setRemoveDiacritics("dbo.RemoveDiacritics");

		var items = server.getItemsToDisplay();
		return ResponseEntity.ok(items);
	}

	@GetMapping(value = {"ordercolumnswithedit", "OrderColumnsWithEdit"}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ItemsDTO<Order>> orderColumnsWithEdit(HttpServletRequest request) {

		EntityManager em = entityManagerFactory.createEntityManager();

		Consumer<IGridColumnCollection<Order>> columns = c -> {
			c.add("orderID", Integer.class);
			c.add("orderDate", LocalDateTime.class, "orderCustomDate");
			c.add("customer.companyName", String.class);
			c.add("customer.contactName", String.class);
			c.add("freight", BigDecimal.class);
			c.add("customer.isVip",Boolean.class);
		};

		IGridServer<Order> server = new GridServer<Order>(em, Order.class, request.getParameterMap(), columns)
            	.withPaging(10)
				.sortable()
				.filterable()
				.setRemoveDiacritics("dbo.RemoveDiacritics");

		var items = server.getItemsToDisplay();
		return ResponseEntity.ok(items);
	}

	@GetMapping(value = {"ordercolumnswithcrud", "OrderColumnsWithCrud"}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ItemsDTO<Order>> orderColumnsWithCrud(HttpServletRequest request) {

		EntityManager em = entityManagerFactory.createEntityManager();

		Consumer<IGridColumnCollection<Order>> columns = c -> {
			c.add("orderID", Integer.class).setPrimaryKey(true);
			c.add("customerID", String.class);
			c.add("employeeID", Integer.class);
			c.add("shipVia", Integer.class, true);
			c.add("orderDate", LocalDateTime.class, "orderCustomDate");
			c.add("customer.companyName", String.class);
			c.add("customer.contactName", String.class);
			c.add("freight", BigDecimal.class);
			c.add("customer.isVip",Boolean.class);
			c.add("requiredDate",  LocalDateTime.class, true);
			c.add("shippedDate",  LocalDateTime.class, true);
			c.add("shipName",  String.class, true);
			c.add("shipAddress",  String.class, true);
			c.add("shipCity",  String.class, true);
			c.add("shipPostalCode",  String.class, true);
			c.add("shipRegion",  String.class, true);
			c.add("shipCountry",  String.class, true);
		};

		IGridServer<Order> server = new GridServer<Order>(em, Order.class, request.getParameterMap(), columns)
            	.withPaging(10)
				.sortable()
				.filterable()
				.setRemoveDiacritics("dbo.RemoveDiacritics");

		var items = server.getItemsToDisplay();
		return ResponseEntity.ok(items);
	}

	@GetMapping(value = {"ordercolumnswithsubgridcrud", "OrderColumnsWithSubgridCrud"}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ItemsDTO<Order>> orderColumnsWithSubgridCrud(HttpServletRequest request)
	{
		EntityManager em = entityManagerFactory.createEntityManager();

		Consumer<IGridColumnCollection<Order>> columns = c -> {
			c.add("orderID", Integer.class).setPrimaryKey(true);
			c.add("customerID", String.class);
			c.add("employeeID", Integer.class);
			c.add("shipVia", Integer.class, true);
			c.add("orderDate", LocalDateTime.class, "orderCustomDate");
			c.add("customer.companyName", String.class);
			c.add("customer.contactName", String.class);
			c.add("freight", BigDecimal.class);
			c.add("customer.isVip",Boolean.class);
			c.add("requiredDate",  LocalDateTime.class, true);
			c.add("shippedDate",  LocalDateTime.class, true);
			c.add("shipName",  String.class, true);
			c.add("shipAddress",  String.class, true);
			c.add("shipCity",  String.class, true);
			c.add("shipPostalCode",  String.class, true);
			c.add("shipRegion",  String.class, true);
			c.add("shipCountry",  String.class, true);
		};

		IGridServer<Order> server = new GridServer<Order>(em, Order.class, request.getParameterMap(), columns)
				.withPaging(10)
				.sortable()
				.filterable()
				.setRemoveDiacritics("dbo.RemoveDiacritics");

		var items = server.getItemsToDisplay();
		return ResponseEntity.ok(items);
	}

	@GetMapping(value = {"getordersgridwithcount", "GetOrdersGridWithCount"}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ItemsDTO<OrderCount>> getOrdersGridWithCount(HttpServletRequest request) {

		EntityManager em = entityManagerFactory.createEntityManager();

		Consumer<IGridColumnCollection<OrderCount>> columns = c -> {
			var count = c.createColumn("orderDetails.count", Collection.class, false, null);
			var freight = c.createColumn("freight", BigDecimal.class, false, null);

			c.add("orderID", Integer.class);
			c.add("orderDate", LocalDateTime.class, "orderCustomDate");
			c.add("customer.companyName", String.class)
					.thenSortBy((GridCoreColumn<OrderCount, String>)count)
					.thenSortByDescending((GridCoreColumn<OrderCount, BigDecimal>)freight);
			c.add("customer.contactName", String.class);
			c.add(freight);
			c.add("customer.isVip",Boolean.class);
			c.add(count).subgrid(OrderDetailCount.class, new Pair[]{new Pair("orderID", "orderID")})
					.sum(true).average(true).max(true).min(true);
		};

		IGridServer<OrderCount> server = new GridServer<>(em, OrderCount.class, request.getParameterMap(), columns)
				.withPaging(10)
				.sortable()
				.filterable()
				.searchable(true, false)
				.setRemoveDiacritics("dbo.RemoveDiacritics");

		var items = server.getItemsToDisplay();
		return ResponseEntity.ok(items);
	}

	@GetMapping(value = {"getordercolumnswitherrors", "GetOrderColumnsWithErrors"}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ItemsDTO<Order>> getOrderColumnsWithErrors(HttpServletRequest request) {

		var random = new Random();
		if (random.nextBoolean())
			return ResponseEntity.badRequest().build();

		EntityManager em = entityManagerFactory.createEntityManager();

		Consumer<IGridColumnCollection<Order>> columns = c -> {
			c.add("orderID", Integer.class);
			c.add("orderDate", LocalDateTime.class, "orderCustomDate");
			c.add("customer.companyName", String.class);
			c.add("customer.contactName", String.class);
			c.add("customer.country", String.class, true);
			c.add("freight", BigDecimal.class);
			c.add("customer.isVip",Boolean.class);
		};

		IGridServer<Order> server = new GridServer<>(em, Order.class, request.getParameterMap(), columns)
				.withPaging(10)
				.sortable()
				.filterable()
				.setRemoveDiacritics("dbo.RemoveDiacritics");

		var items = server.getItemsToDisplay();
		return ResponseEntity.ok(items);
	}

	@GetMapping(value = {"getvirtualizedordersgrid", "GetVirtualizedOrdersGrid"}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ItemsDTO<Order>> getVirtualizedOrdersGrid(HttpServletRequest request) {

		EntityManager em = entityManagerFactory.createEntityManager();

		Consumer<IGridColumnCollection<Order>> columns = c -> {
			c.add("orderID", Integer.class).setPrimaryKey(true);
			c.add("customerID", String.class);
			c.add("employeeID", Integer.class);
			c.add("shipVia", Integer.class, true);
			c.add("orderDate", LocalDateTime.class, "orderCustomDate")
					.max(true).min(true);
			c.add("customer.companyName", String.class)
					.max(true).min(true);
			c.add("customer.contactName", String.class)
					.max(true).min(true);
			c.add("freight", BigDecimal.class)
					.sum(true).average(true).max(true).min(true);
			c.add("customer.isVip",Boolean.class);
			c.add("requiredDate",  LocalDateTime.class, true);
			c.add("shippedDate",  LocalDateTime.class, true);
			c.add("shipName",  String.class, true);
			c.add("shipAddress",  String.class, true);
			c.add("shipCity",  String.class, true);
			c.add("shipPostalCode",  String.class, true);
			c.add("shipRegion",  String.class, true);
			c.add("shipCountry",  String.class, true);
		};

		IGridServer<Order> server = new GridServer<Order>(em, Order.class, request.getParameterMap(), columns)
            	.sortable()
				.filterable()
				.setRemoveDiacritics("dbo.RemoveDiacritics");

		var items = server.getItemsToDisplay();
		return ResponseEntity.ok(items);
	}

	@GetMapping(value = {"getordersgridallfeatures", "GetOrdersGridAllFeatures"}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ItemsDTO<Order>> getOrdersGridAllFeatures(HttpServletRequest request) {

		EntityManager em = entityManagerFactory.createEntityManager();

		Consumer<IGridColumnCollection<Order>> columns = c -> {
			c.add("orderID", Integer.class).setPrimaryKey(true);
			c.add("customerID", String.class);
			c.add("employeeID", Integer.class);
			c.add("shipVia", Integer.class, true);
			c.add("orderDate", LocalDateTime.class, "orderCustomDate")
					.max(true).min(true);
			c.add("customer.companyName", String.class)
					.max(true).min(true);
			c.add("customer.contactName", String.class)
					.max(true).min(true);
			c.add("freight", BigDecimal.class)
					.sum(true).average(true).max(true).min(true);
			c.add("customer.isVip",Boolean.class);
			c.add("requiredDate",  LocalDateTime.class, true);
			c.add("shippedDate",  LocalDateTime.class, true);
			c.add("shipName",  String.class, true);
			c.add("shipAddress",  String.class, true);
			c.add("shipCity",  String.class, true);
			c.add("shipPostalCode",  String.class, true);
			c.add("shipRegion",  String.class, true);
			c.add("shipCountry",  String.class, true);
		};

		IGridServer<Order> server = new GridServer<Order>(em, Order.class, request.getParameterMap(), columns)
            	.withPaging(10)
				.sortable()
				.filterable()
				.searchable(true, false)
				.setRemoveDiacritics("dbo.RemoveDiacritics");

		var items = server.getItemsToDisplay();
		return ResponseEntity.ok(items);
	}

	@GetMapping(value = {"getcustomersgrid", "GetCustomersGrid"}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ItemsDTO<Customer>> GetCustomersGrid(HttpServletRequest request) {

		EntityManager em = entityManagerFactory.createEntityManager();

		Consumer<IGridColumnCollection<Customer>> columns = c -> {
			c.add("customerID", String.class);
			c.add("companyName", String.class);
			c.add("contactName", String.class);
			c.add("address", String.class);
			c.add("city", String.class);
			c.add("postalCode", String.class);
			c.add("country", String.class);
		};

		IGridServer<Customer> server = new GridServer<Customer>(em, Customer.class, request.getParameterMap(), columns)
				.withPaging(10)
				.sortable()
				.filterable()
				.setRemoveDiacritics("dbo.RemoveDiacritics");

		var items = server.getItemsToDisplay();
		return ResponseEntity.ok(items);
	}

	@GetMapping(value = {"getemployeesgrid", "GetEmployeesGrid"}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ItemsDTO<Employee>>  getEmployeesGrid(HttpServletRequest request) {

		EntityManager em = entityManagerFactory.createEntityManager();

		Consumer<IGridColumnCollection<Employee>> columns = c -> {
			c.add("employeeID", Integer.class).setPrimaryKey(true);
			c.add("titleOfCourtesy", String.class);
			c.add("firstName", String.class);
			c.add("lastName", String.class);
			c.add("title", String.class);
			c.add("birthDate", LocalDateTime.class, true);
			c.add("hireDate", LocalDateTime.class);
			c.add("address", String.class, true);
			c.add("city", String.class, true);
			c.add("region", String.class,true);
			c.add("postalCode", String.class, true);
			c.add("country", String.class, true);
			c.add("homePhone", String.class, true);
			c.add("extension", String.class, true);
			c.add("eportsTo", Employee.class, true);
			c.add("notes", String.class, true);
			c.add("photoPath", String.class, true);
		};

		IGridServer<Employee> server = new GridServer<Employee>(em, Employee.class, request.getParameterMap(), columns)
				.withPaging(10)
				.sortable()
				.filterable()
				.setRemoveDiacritics("dbo.RemoveDiacritics");

		var items = server.getItemsToDisplay();
		return ResponseEntity.ok(items);
	}

	@PostMapping(value = {"setemployeephoto", "SetEmployeePhoto"}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> setEmployeePhoto(@RequestBody Employee employee) {

		try {
			var attachedEmployee = employeeRepository.findById(employee.getEmployeeID());
			if (attachedEmployee.isPresent())
			{
				var photo = employee.getBase64String();
				// convert url scaped base64 to byte array
				photo = photo.replace('.', '+');
				photo = photo.replace('_', '/');
				photo = photo.replace('-', '=');
				attachedEmployee.get().setPhoto(Base64.getDecoder().decode(photo));
				employeeRepository.saveAndFlush(attachedEmployee.get());
			}
			return ResponseEntity.noContent().build();
		}
		catch (Exception e) {
			return ResponseEntity.badRequest()
					.body(e.getMessage().replace('{', '(').replace('}', ')'));
		}
	}

	@GetMapping(value = {"getcustomersnames", "GetCustomersNames"}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity getCustomersNames(HttpServletRequest request) {

		EntityManager em = entityManagerFactory.createEntityManager();

		Consumer<IGridColumnCollection<Order>> columns = c -> {
			c.add("orderID", Integer.class);
			c.add("orderDate", LocalDateTime.class, "orderCustomDate");
			c.add("customer.companyName", String.class);
			c.add("customer.contactName", String.class);
			c.add("customer.country", String.class, true);
			c.add("freight", BigDecimal.class);
			c.add("customer.isVip",Boolean.class);
		};

		// get all customer ids in the grid with the current filters
		var server = new GridServer<>(em, Order.class, request.getParameterMap(), columns);

		// get the full predicate (after pre-process)
		var grid = server.getGrid();
		grid.getPredicate();

		// create a query spec to copy the predicate to a new query
		var gridQuery = (SqmSelectStatement) grid.getCriteriaQuery();
		var gridQuerySpec = gridQuery.getQuerySpec();
		var gridSubQuerySpec = gridQuerySpec.copy(SqmCopyContext.simpleContext());

		// create a new query to get company names
		var newBuilder = (HibernateCriteriaBuilder) grid.getCriteriaBuilder();
		var columnNameQuery = newBuilder.createQuery(String.class);

		// copy the former predicate using its spec to a subquery of the new query to get distinct company names
		var subQuery = (SqmSubQuery<Tuple>) columnNameQuery.subquery(Tuple.class);
		subQuery.setQueryPart(gridSubQuerySpec);
		Root<?> subQueryRoot = subQuery.getRootList().get(0);
		subQuery.multiselect(subQueryRoot.get("customer").get("companyName").alias("companyName"))
				.distinct(true);

		// configure the new query from the copied subquery
		columnNameQuery.from(subQuery);
		Root<?> columnNameRoot = columnNameQuery.getRootList().get(0);
		columnNameQuery.select(columnNameRoot.get("companyName"));

		var customerNames = grid.getEntityManager().createQuery(columnNameQuery).getResultList();
		return ResponseEntity.ok(customerNames);
	}

	@GetMapping(value = {"getallcustomers", "GetAllCustomers"}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAllCustomers() {

		return ResponseEntity.ok(customerRepository.findAll().stream()
				.map(r -> new SelectItem(r.getCustomerID(), r.getCustomerID() + " - " + r.getCompanyName()))
				.toList());
	}

	@GetMapping(value = {"getallcustomers2", "GetAllCustomers2"}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAllCustomers2() {

		return ResponseEntity.ok(customerRepository.findAll().stream()
				.map(r -> new SelectItem(r.getCompanyName(), r.getCompanyName()))
				.toList());
	}

	@GetMapping(value = {"getallcontacts", "GetAllContacts"}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAllContacts() {

		return ResponseEntity.ok(customerRepository.findAll().stream()
				.map(r -> new SelectItem(r.getContactName(), r.getContactName()))
				.toList());
	}

	@GetMapping(value = {"getallemployees", "GetAllEmployees"}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAllEmployees() {

		return ResponseEntity.ok(employeeRepository.findAll().stream()
				.map(r -> new SelectItem(r.getEmployeeID().toString(), r.getEmployeeID().toString() + " - "
						+ r.getFirstName() + " " + r.getLastName()))
				.toList());
	}

	@GetMapping(value = {"getallshippers", "GetAllShippers"}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAllShippers() {

		return ResponseEntity.ok(shipperRepository.findAll().stream()
				.map(r -> new SelectItem(r.getShipperID().toString(), r.getShipperID().toString() + " - "
						+ r.getCompanyName()))
				.toList());
	}

	@GetMapping(value = {"getallproducts", "GetAllProducts"}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAllProducts() {

		return ResponseEntity.ok(productRepository.findAll().stream()
				.map(r -> new SelectItem(r.getProductID().toString(), r.getProductID().toString() + " - "
						+ r.getProductName()))
				.toList());
	}

	@GetMapping(value = {"getorderdetailsgrid", "GetOrderDetailsGrid"}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getOrderDetailsGrid(HttpServletRequest request,
														 @RequestParam(value = "OrderId") int orderId) {

		EntityManager em = entityManagerFactory.createEntityManager();

		Consumer<IGridColumnCollection<OrderDetail>> columns = c -> {
			c.add("orderID", Integer.class, true).setPrimaryKey(true);
			c.add("productID", Integer.class).setPrimaryKey(true);
			c.add("product.productName", String.class);
			c.add("quantity", Short.class);
			c.add("unitPrice", BigDecimal.class);
			c.add("discount", Float.class);
		};

		var server = new GridServer<>(em, OrderDetail.class, request.getParameterMap(), columns)
				.withPaging(10)
				.sortable()
				.filterable();

		CriteriaBuilder cb = server.getGrid().getCriteriaBuilder();
		Root<OrderDetail> root = server.getGrid().getRoot();
		Predicate predicate = cb.equal(root.get("orderID"), orderId);
		server.setPredicate(predicate);

		var items = server.getItemsToDisplay();
		return ResponseEntity.ok(items);
	}

	@PostMapping(value = {"add1tofreight", "Add1ToFreight"}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> add1ToFreight(@RequestParam(value = "id") int id) {

		var order =  orderRepository.findById(id);
		if (order.isPresent() && order.get().getFreight() != null) {
			order.get().setFreight(order.get().getFreight() + 1);
			try {
				orderRepository.saveAndFlush(order.get());
				return ResponseEntity.ok().build();
			}
			catch (Exception e) {
				return ResponseEntity.badRequest()
						.body(e.getMessage().replace('{', '(').replace('}', ')'));
			}
		}
		return ResponseEntity.badRequest().build();
	}

	@PostMapping(value = {"subtract1tofreight", "Subtract1ToFreight"}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> subtract1ToFreight(@RequestParam(value = "id") int id)
	{
		var order =  orderRepository.findById(id);
		if (order.isPresent() && order.get().getFreight() != null) {
			order.get().setFreight(order.get().getFreight() - 1);
			try {
				orderRepository.saveAndFlush(order.get());
				return ResponseEntity.ok().build();
			}
			catch (Exception e) {
				return ResponseEntity.badRequest()
						.body(e.getMessage().replace('{', '(').replace('}', ')'));
			}
		}
		return ResponseEntity.badRequest().build();
	}
}