# DecentralizedStableCoin - A Defi Lending protocol
>
>Decentralized stable coin is a defi protocol built using foundry andsolidity. It allows users to deposit DAI, ETH or BTC as collateral and mint a custom stable coin pegged to the dollar.
---

## Features

- Deposit DAI/ETH/BTC and mint a custome stablecoin pegged to the dollar.
- Mint an amount of the stable coin if you have already deposited an equivalent amount in collateral.
- Burn a specified amount of the stable coin (usually done if a user is under-collateralized and does not want to be liquidated).
- Liquidation of all under-collateralized users.
- over-collateralization logic (Users must deposit 150% collateral for the stablecoin amount they want to mint).
- Make a collateral redeem request to redeem the specified amount of collateral to be redeemed. An equivalent amount of the user's stable coin will be burnt respectively
- On-chain Chainlink oracles.

---

## Tech Stack

- Solidity
- foundry
- openzeppelin

## Foundry

**Foundry is a blazing fast, portable and modular toolkit for Ethereum application development written in Rust.**

Foundry consists of:

- **Forge**: Ethereum testing framework (like Truffle, Hardhat and DappTools).
- **Cast**: Swiss army knife for interacting with EVM smart contracts, sending transactions and getting chain data.
- **Anvil**: Local Ethereum node, akin to Ganache, Hardhat Network.
- **Chisel**: Fast, utilitarian, and verbose solidity REPL.

## Documentation

<https://book.getfoundry.sh/>

## Usage

### Build

```shell
forge build
```

### Test

```shell
forge test
```

### Format

```shell
forge fmt
```

### Gas Snapshots

```shell
forge snapshot
```

### Anvil

```shell
anvil
```

### Deploy

```shell
forge script script/Counter.s.sol:CounterScript --rpc-url <your_rpc_url> --private-key <your_private_key>
```

### Test

```shell
$ forge test -m function to test      
>Use this to test your preferred test function by providing the function name 


$ forge test --fork-url <your_rpc_url>     
>Use this to test a function or contract on your preferred chain by specifying the RPC_URL for the chain

$ forge test --match-path test/contractA.t.sol   
>Use this to test a particular test contract by specifying test contract class  


$ forge test        
>Forge will look for the tests anywhere in your source directory. Any contract with a function that starts with test is considered to be a test. Usually, tests will be placed in test/ by convention and end with .t.sol .

```

## Logs and traces
>
>The default behavior for forge test is to only display a summary of passing and failing tests. You
can control this behavior by increasing the verbosity (using the -v flag). Each level of verbosity adds
more information:
Level 2 ( -vv ): Logs emitted during tests are also displayed. That includes assertion errors
from tests, showing information such as expected vs actual.
Level 3 ( -vvv ): Stack traces for failing tests are also displayed.
Level 4 ( -vvvv ): Stack traces for all tests are displayed, and setup traces for failing tests are
displayed.
Level 5 ( -vvvvv ): Stack traces and setup traces are always displayed.

### Cast

```shell
cast <subcommand>
```

### Help

```shell
forge --help
anvil --help
cast --help

```

### Working on an Existing Project
>
>Foundry makes developing with existing projects have no overhead.
First, clone the project and run forge install inside the project directory.
We run forge install to install the submodule dependencies that are in the project.
To build, use forge build :
And to test, use forge test :

```shell
$ git clone https://github.com/HAMZA-DANYAL/SmartContracts/DECENTRALIZEDSTABLECOIN
cd DECENTRALIZEDSTABLECOIN
forge install
```
