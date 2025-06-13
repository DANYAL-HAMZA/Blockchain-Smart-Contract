// SPDX-License-Identifier: UNLICENSED
pragma solidity ^0.8.0;

import {ERC20} from "./ERC20.sol";
import {ERC20Burnable} from "./ERC20Burnable.sol";
import {Ownable} from "./Ownable.sol";


contract DecentralizedCoin is ERC20, ERC20Burnable, Ownable {

    address private immutable ownerAddress = 0x78723527393356e3BB25D030720E692eF3Cbb242;


    error DecentralizedStableCoin__AmountMustBeMoreThanZero();
       error DecentralizedStableCoin__BurnAmountExceedsBalance();
       error DecentralizedStableCoin__NotZeroAddress();


   constructor() ERC20("DANYAL","DAN") Ownable(ownerAddress) { }

        function burn(address _from, uint256 _amount) public  onlyOwner returns (bool){
        uint256 balance = balanceOf(_from);
        if (_amount <= 0) {
        revert DecentralizedStableCoin__AmountMustBeMoreThanZero();
        }
        if (balance < _amount) {
        revert DecentralizedStableCoin__BurnAmountExceedsBalance();
        }

        _burn(_from,_amount);
            return true;
        }

        function mint(address _to, uint256 _amount) public  onlyOwner returns (bool) {
        if (_to == address(0)) {
        revert DecentralizedStableCoin__NotZeroAddress();
        }
        if (_amount <= 0) {
        revert DecentralizedStableCoin__AmountMustBeMoreThanZero();
        }
        _mint(_to, _amount);
            return true;
        }

        }




