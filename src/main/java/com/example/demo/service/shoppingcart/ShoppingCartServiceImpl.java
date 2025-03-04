package com.example.demo.service.shoppingcart;

import com.example.demo.dto.cartitem.CreateCartItemRequestDto;
import com.example.demo.dto.cartitem.UpdateCartItemRequestDto;
import com.example.demo.dto.shoppingcart.ShoppingCartResponseDto;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.mapper.BookMapper;
import com.example.demo.mapper.CartItemMapper;
import com.example.demo.mapper.ShoppingCartMapper;
import com.example.demo.model.Book;
import com.example.demo.model.CartItem;
import com.example.demo.model.ShoppingCart;
import com.example.demo.model.User;
import com.example.demo.repository.book.BookRepository;
import com.example.demo.repository.cartitem.CartItemRepository;
import com.example.demo.repository.shoppingcart.ShoppingCartRepository;
import jakarta.transaction.Transactional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;
    private final BookMapper bookMapper;

    @Override
    @Transactional
    public ShoppingCartResponseDto addCartItem(Long userId,
                                               CreateCartItemRequestDto requestDto) {
        Book book = bookMapper.bookFromId(requestDto.getBookId(), bookRepository);
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId);
        Set<CartItem> cartItems = shoppingCart.getCartItems();

        boolean bookExists = false;
        for (CartItem cartItem : cartItems) {
            if (cartItem.getBook().equals(book)) {
                cartItem.setQuantity(cartItem.getQuantity() + requestDto.getQuantity());
                cartItemRepository.save(cartItem);
                bookExists = true;
                break;
            }
        }

        if (!bookExists) {
            CartItem newCartItem = new CartItem();
            newCartItem.setBook(book);
            newCartItem.setQuantity(requestDto.getQuantity());
            shoppingCart.addCartItem(newCartItem);
            cartItemRepository.save(newCartItem);
        }

        return shoppingCartMapper.toShoppingCartResponseDto(shoppingCart);
    }

    @Override
    @Transactional
    public void createUsersCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
    }

    @Override
    @Transactional
    public ShoppingCartResponseDto getUsersCart(Long userId) {
        return shoppingCartMapper
                .toShoppingCartResponseDto(shoppingCartRepository.findByUserId(userId));
    }

    @Override
    @Transactional
    public ShoppingCartResponseDto updateCartItemQuantity(User user,
                                                          Long cartItemId,
                                                          UpdateCartItemRequestDto requestDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(user.getId());

        CartItem cartItem = cartItemRepository.findByIdAndShoppingCartId(cartItemId,
                        shoppingCart.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cart item not found with ID: " + cartItemId
                + " for shopping cart " + shoppingCart.getId()));
        cartItem.setQuantity(requestDto.getQuantity());

        shoppingCart.addCartItem(cartItem);
        cartItemRepository.save(cartItem);

        ShoppingCartResponseDto shoppingCartResponseDto = shoppingCartMapper
                .toShoppingCartResponseDto(shoppingCart);
        shoppingCartResponseDto.setCartItems(shoppingCart.getCartItems().stream()
                .map(cartItemMapper::toCartItemResponseDto)
                .toList());

        return shoppingCartResponseDto;
    }

    @Override
    public void removeCartItem(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cart item not found for ID: " + cartItemId));
        cartItemRepository.delete(cartItem);
    }
}
