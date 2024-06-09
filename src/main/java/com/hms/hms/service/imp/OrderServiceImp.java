package com.jed9h3.inventorymanagementsystem.service.imp;

import com.jed9h3.inventorymanagementsystem.dto.OrderDto;
import com.jed9h3.inventorymanagementsystem.entity.Order;
import com.jed9h3.inventorymanagementsystem.exception.BadRequestException;
import com.jed9h3.inventorymanagementsystem.exception.NoContentException;
import com.jed9h3.inventorymanagementsystem.exception.NotFoundException;
import com.jed9h3.inventorymanagementsystem.repository.OrderRepository;
import com.jed9h3.inventorymanagementsystem.service.CustomerService;
import com.jed9h3.inventorymanagementsystem.service.InventoryService;
import com.jed9h3.inventorymanagementsystem.service.ItemService;
import com.jed9h3.inventorymanagementsystem.service.OrderService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class OrderServiceImp implements OrderService {
    private final OrderRepository orderRepository;
    private final CustomerService customerService;
    private final ItemService itemService;
    private final InventoryService inventoryService;

    public OrderServiceImp(OrderRepository orderRepository, CustomerService customerService, ItemService itemService, InventoryService inventoryService) {
        this.orderRepository = orderRepository;
        this.customerService = customerService;
        this.itemService = itemService;
        this.inventoryService = inventoryService;
    }

    @Override
    public List<OrderDto> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        if (orders.isEmpty()) {
            throw new NoContentException("No orders registered yet");
        }
        return orders.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public OrderDto createOrder(OrderDto orderDto) {
        System.out.println(orderDto.getItemId()+"s--------");
        checkQuantity(orderDto.getItemId(),orderDto.getOrderedQuantity(),0L);
        BigDecimal remaining=calculateTotal(orderDto.getItemId(),orderDto.getOrderedQuantity());
        checkBalance(orderDto.getCustomerId(),remaining, BigDecimal.valueOf(0));
        adjustBalance(orderDto.getCustomerId(),remaining,false);
        adjustQuantity(orderDto.getItemId(),orderDto.getOrderedQuantity(),false);
        Order order = convertToEntity(orderDto);
        System.out.println(order.getItem().getItemId()+"s--------");
        Order savedOrder = orderRepository.save(order);
        System.out.println(savedOrder.getItem().getItemId()+"s--------");
        return convertToDto(savedOrder);
    }

    @Override
    public void deleteAllOrders() {
        if (orderRepository.count()==0){
            throw new NoContentException("No orders registered yet to delete");
        }else{
            List<OrderDto> orders = getAllOrders();
            for (OrderDto orderDto : orders) {
                BigDecimal remaining=calculateTotal(orderDto.getItemId(),orderDto.getOrderedQuantity());
                adjustBalance(orderDto.getCustomerId(),remaining,true);
                adjustQuantity(orderDto.getItemId(),orderDto.getOrderedQuantity(),true);
            }
            orderRepository.deleteAll();
        }
    }

    @Override
    public OrderDto getOrderById(long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new NotFoundException("Order with id "+id+" doesnt exist"));
        return convertToDto(order);
    }

    @Override
    public OrderDto updateOrderById(OrderDto orderDto, long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new NotFoundException("Order with id "+id+" doesnt exist"));
        Long cusid=orderDto.getCustomerId();
        Long imid=orderDto.getItemId();
        Long qua=orderDto.getOrderedQuantity();
        if (cusid==null || imid==null || qua==null){
            throw new BadRequestException("Request missing required attributes");
        }
        if (!cusid.equals(order.getCustomer().getCustomerId())){
            throw new BadRequestException("Customer id cannot be updated");
        }


        Long tempQ=order.getOrderedQuantity();
        BigDecimal oldremaining=calculateTotal(order.getCustomer().getCustomerId(),tempQ);
        BigDecimal newremaining;
        if (imid.equals(order.getItem().getItemId()) && qua.equals(tempQ)){
            throw new NoContentException("No attributes to be updated");
        }
        if (imid.equals(order.getItem().getItemId())){
            checkQuantity(imid,qua,tempQ);
        }else {
            checkQuantity(imid,qua,0L);
        }
        newremaining=calculateTotal(imid,qua);
        checkBalance(cusid,newremaining,oldremaining);
        adjustQuantity(order.getItem().getItemId(),tempQ,true);
        adjustBalance(cusid,oldremaining,true);
        adjustQuantity(imid,qua,false);
        order.setItem(itemService.getRawItemById(imid));
        order.setOrderedQuantity(qua);
        adjustBalance(cusid,newremaining,false);

        Order savedOrder = orderRepository.save(order);
        return convertToDto(savedOrder);
    }

    @Override
    public OrderDto partiallyUpdateOrderById(OrderDto orderDto, long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new NotFoundException("Order with id "+id+" doesnt exist"));
        Long cusid=orderDto.getCustomerId();
        Long imid=orderDto.getItemId();
        Long qua=orderDto.getOrderedQuantity();
        if (cusid!=null && !cusid.equals(order.getCustomer().getCustomerId())){
            throw new BadRequestException("Customer id cannot be updated");
        }
        if (imid==null && qua==null){
            throw new NoContentException("No attributes to be updated");
        }
        cusid=order.getCustomer().getCustomerId();
        Long tempQ=order.getOrderedQuantity();
        BigDecimal oldremaining=calculateTotal(order.getCustomer().getCustomerId(),tempQ);
        BigDecimal newremaining;
        if(imid!=null && qua!=null){
            if (imid.equals(order.getItem().getItemId()) && qua.equals(tempQ)){
                throw new NoContentException("No attributes to be updated");
            }
            if (imid.equals(order.getItem().getItemId())){
                checkQuantity(imid,qua,tempQ);
            }else {
                checkQuantity(imid,qua,0L);
            }
            newremaining=calculateTotal(imid,qua);
            checkBalance(cusid,newremaining,oldremaining);
            adjustQuantity(order.getItem().getItemId(),tempQ,true);
            adjustBalance(cusid,oldremaining,true);

            adjustQuantity(imid,qua,false);
            order.setItem(itemService.getRawItemById(imid));
            order.setOrderedQuantity(qua);
            adjustBalance(cusid,newremaining,false);
        }else if (imid!=null){
            if (imid.equals(order.getItem().getItemId())){
                throw new NoContentException("No attributes to be updated");
            }
            checkQuantity(imid,tempQ,0L);

            newremaining=calculateTotal(imid,tempQ);
            checkBalance(cusid,newremaining,oldremaining);
            adjustQuantity(order.getItem().getItemId(),tempQ,true);
            adjustBalance(cusid,oldremaining,true);

            adjustQuantity(imid,tempQ,false);
            order.setItem(itemService.getRawItemById(imid));
            adjustBalance(cusid,oldremaining,false);
        }else {
            if (qua.equals(tempQ)){
                throw new NoContentException("No attributes to be updated");
            }
            checkQuantity(order.getCustomer().getCustomerId(),qua,tempQ);
            newremaining=calculateTotal(order.getCustomer().getCustomerId(),qua);
            checkBalance(cusid,newremaining,oldremaining);
            adjustQuantity(order.getItem().getItemId(),tempQ,true);
            adjustBalance(cusid,oldremaining,true);

            adjustQuantity(order.getItem().getItemId(),qua,false);
            order.setOrderedQuantity(qua);
            adjustBalance(order.getCustomer().getCustomerId(),newremaining,false);
        }
        Order savedOrder = orderRepository.save(order);
        return convertToDto(savedOrder);
    }

    @Override
    public void deleteOrderById(long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new NotFoundException("Order with id "+id+" doesnt exist"));
        adjustQuantity(order.getItem().getItemId(),order.getOrderedQuantity(),true);
        BigDecimal remaining=calculateTotal(order.getCustomer().getCustomerId(),order.getOrderedQuantity());
        adjustBalance(order.getCustomer().getCustomerId(),remaining,true);
        orderRepository.delete(order);
    }

    private OrderDto convertToDto(Order order) {
        OrderDto result = new OrderDto();
        result.setOrderId(order.getOrderId());
        result.setCustomerId(order.getCustomer().getCustomerId());
        result.setItemId(order.getItem().getItemId());
        result.setOrderedQuantity(order.getOrderedQuantity());
        return result;
    }

    private Order convertToEntity(OrderDto orderDto) {
        Order result = new Order();
        result.setItem(itemService.getRawItemById(orderDto.getItemId()));
        result.setCustomer(customerService.getRawCustomerById(orderDto.getCustomerId()));
        result.setOrderedQuantity(orderDto.getOrderedQuantity());
        return result;
    }
    private boolean checkAvailability(Long imid,Long qua,Long bias){
        Long avqua=inventoryService.getInventoryById(imid).getAvailableQuantity();
        return qua <= avqua+bias;
    }
    private void adjustQuantity(Long imid,Long qua,boolean sign){
        Long avqua=inventoryService.getInventoryById(imid).getAvailableQuantity();
        if (sign){
            inventoryService.getInventoryById(imid).setAvailableQuantity(avqua+qua);
        }else{
            inventoryService.getInventoryById(imid).setAvailableQuantity(avqua-qua);
        }
    }
    private void checkQuantity(Long imid,Long qua,Long bias){
        if (qua<=0){
            throw new BadRequestException("Order quantity cannot be negative or zero");
        }
        if (!checkAvailability(imid,qua,bias)){
            throw new BadRequestException("Quantity exceeds inventory");
        }
    }
    private void checkBalance(Long cusid,BigDecimal total,BigDecimal bias){
        BigDecimal balance=customerService.getCustomerById(cusid).getBalance();
        if ((balance.add(bias)).compareTo(total) < 0){
            throw new BadRequestException("Total Price exceeds Customer balance");
        }
    }
    private BigDecimal calculateTotal(Long itemId,Long qua){
        BigDecimal price=itemService.getItemById(itemId).getPrice();
        return price.multiply(BigDecimal.valueOf(qua));
    }
    private void adjustBalance(Long cusid,BigDecimal remaining,boolean sign){
        BigDecimal balance=customerService.getCustomerById(cusid).getBalance();
        if (sign){
            customerService.getCustomerById(cusid).setBalance(balance.add(remaining));
        }else{
            customerService.getCustomerById(cusid).setBalance(balance.subtract(remaining));
        }
    }
}
