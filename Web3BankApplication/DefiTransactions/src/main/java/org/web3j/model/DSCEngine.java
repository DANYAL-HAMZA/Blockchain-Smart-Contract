package org.web3j.model;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple2;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.11.0.
 */
@SuppressWarnings("rawtypes")
public class DSCEngine extends Contract {
    public static final String BINARY = "60c06040527378723527393356e3bb25d030720e692ef3cbb24260a052348015610027575f80fd5b50604051612550380380612550833981016040819052610046916102c7565b60a05160015f556001600160a01b03811661007a57604051631e4fbdf760e01b81525f600482015260240160405180910390fd5b610083816101a4565b506001805460ff60a01b1916905581518351146100b357604051631d8bb3e760e11b815260040160405180910390fd5b5f5b835181101561018f578281815181106100d0576100d061033b565b602002602001015160025f8684815181106100ed576100ed61033b565b60200260200101516001600160a01b03166001600160a01b031681526020019081526020015f205f6101000a8154816001600160a01b0302191690836001600160a01b03160217905550600884828151811061014b5761014b61033b565b6020908102919091018101518254600180820185555f9485529290932090920180546001600160a01b0319166001600160a01b0390931692909217909155016100b5565b506001600160a01b03166080525061034f9050565b600180546001600160a01b038381166001600160a01b0319831681179093556040519116919082907f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e0905f90a35050565b634e487b7160e01b5f52604160045260245ffd5b80516001600160a01b038116811461021f575f80fd5b919050565b5f82601f830112610233575f80fd5b81516001600160401b0381111561024c5761024c6101f5565b604051600582901b90603f8201601f191681016001600160401b038111828210171561027a5761027a6101f5565b604052918252602081850181019290810186841115610297575f80fd5b6020860192505b838310156102bd576102af83610209565b81526020928301920161029e565b5095945050505050565b5f805f606084860312156102d9575f80fd5b83516001600160401b038111156102ee575f80fd5b6102fa86828701610224565b602086015190945090506001600160401b03811115610317575f80fd5b61032386828701610224565b92505061033260408501610209565b90509250925092565b634e487b7160e01b5f52603260045260245ffd5b60805160a0516121b261039e5f395f50505f8181610556015281816106d5015281816109b201528181610c2901528181610f0c01528181610fcb0152818161146b015261165301526121b25ff3fe608060405234801561000f575f80fd5b506004361061024a575f3560e01c80638c1ae6c811610140578063b6b7c2ad116100bf578063deb8e01811610084578063deb8e01814610554578063e2ec87801461057a578063f2fde38b1461058d578063f7b188a5146105a0578063f970c3b7146105a8578063fe6bcd7c146105bb575f80fd5b8063b6b7c2ad146104f5578063bf2791f614610508578063c18764531461051b578063c660d1121461052e578063d55b5b0a14610541575f80fd5b8063aa70762b11610105578063aa70762b146104ac578063aab3f868146104bf578063abd24198146104d2578063afea2e48146104da578063b58eb63f146104ed575f80fd5b80638c1ae6c81461046d5780638da5cb5b1461047b5780638f63d6671461048c57806390b1c85d146104975780639670c0bc1461046d575f80fd5b80635b3ea09b116101cc578063715018a611610191578063715018a6146103fa5780637be564fc146104025780637d1a44501461042a5780638456cb591461043d57806388b7e8b414610445575f80fd5b80635b3ea09b146103875780635c975abb1461039a5780636b57160d146103b85780636c8102c0146103e05780636de152b5146103e7575f80fd5b80633d8a8c2f116102125780633d8a8c2f1461033e5780634ae9b8bc146103535780634df34f241461035a578063556bfd9c1461036d57806359aa9e7214610380575f80fd5b806301f728841461024e57806309e2d2e5146102745780631c08adda1461029b57806331e92b83146102de57806335ab842f14610316575b5f80fd5b61026161025c366004611dd4565b6105ce565b6040519081526020015b60405180910390f35b61027c6105e2565b604080516001600160a01b03909316835260208301919091520161026b565b6102c66102a9366004611e0f565b6001600160a01b039081165f908152600260205260409020541690565b6040516001600160a01b03909116815260200161026b565b6102616102ec366004611e28565b6001600160a01b039182165f90815260036020908152604080832093909416825291909152205490565b610261610324366004611e0f565b6001600160a01b03165f9081526006602052604090205490565b61035161034c366004611e59565b610664565b005b6032610261565b610351610368366004611e59565b610834565b61035161037b366004611e81565b610870565b600a610261565b610351610395366004611eba565b61091c565b600154600160a01b900460ff165b604051901515815260200161026b565b6102616103c6366004611e0f565b6001600160a01b03165f9081526004602052604090205490565b6064610261565b6103516103f5366004611e59565b610bb7565b610351610d75565b610415610410366004611e0f565b610d88565b6040805192835260208301919091520161026b565b610261610438366004611e0f565b610d9c565b610351610e16565b610261610453366004611e0f565b6001600160a01b03165f9081526007602052604090205490565b670de0b6b3a7640000610261565b6001546001600160a01b03166102c6565b6402540be400610261565b61049f610e2e565b60405161026b9190611f04565b6103516104ba366004611f4f565b610e8e565b6103516104cd366004611f7f565b6110c8565b61049f611269565b6102616104e8366004611e59565b6112c7565b61049f611380565b610351610503366004611e59565b6113de565b610351610516366004611e81565b61157f565b6103a8610529366004611e0f565b611630565b61026161053c366004611e59565b6116c3565b61035161054f366004611e81565b6116ce565b7f00000000000000000000000000000000000000000000000000000000000000006102c6565b610261610588366004611fc7565b611766565b61035161059b366004611e0f565b61177e565b6103516117bb565b6103516105b6366004611fde565b6117d3565b6102616105c9366004611e0f565b611972565b5f6105d9838361197c565b90505b92915050565b5f80808060015b600954811161065b575f6009828154811061060657610606612018565b5f9182526020822001546001600160a01b0316915080610625836119cb565b9150915081811015610645575f61063c8284612040565b96509294508492505b505050808061065390612053565b9150506105e9565b50939092509050565b80805f036106855760405163cb1d3f7d60e01b815260040160405180910390fd5b61068d6119f4565b610695611a1c565b61069d611a49565b6001600160a01b0383165f90815260056020526040812080548492906106c490849061206b565b909155505f90506001600160a01b037f0000000000000000000000000000000000000000000000000000000000000000166340c10f198561070d670de0b6b3a76400008761207e565b6040516001600160e01b031960e085901b1681526001600160a01b03909216600483015260248201526044016020604051808303815f875af1158015610755573d5f803e3d5ffd5b505050506040513d601f19601f820116820180604052508101906107799190612095565b905060018115151461079e576040516330eb0fc960e01b815260040160405180910390fd5b600a80546001810182555f919091527fc65a7bb8d6351c1cf70c95a316cc6a92839c986682d98bc35f958f4883f9d2a80180546001600160a01b0319166001600160a01b03861690811790915560408051918252602082018590527f39c9d798646325a7efa26a00431d9de03e78f3a4d29a90479f2fd0613fcf8a5791015b60405180910390a15061082f60015f55565b505050565b80805f036108555760405163cb1d3f7d60e01b815260040160405180910390fd5b61085d611a49565b61086783836113de565b61082f83611a74565b81805f036108915760405163cb1d3f7d60e01b815260040160405180910390fd5b6108996119f4565b6001600160a01b038085165f908152600260205260409020548591166108e25760405163cf39359560e01b81526001600160a01b03821660048201526024015b60405180910390fd5b6108ea611a49565b5f6108f586866112c7565b9050610902868286611aac565b61090b84611a74565b505061091660015f55565b50505050565b81805f0361093d5760405163cb1d3f7d60e01b815260040160405180910390fd5b6001600160a01b038083165f908152600260205260409020548391166109815760405163cf39359560e01b81526001600160a01b03821660048201526024016108d9565b6109896119f4565b610991611a49565b6040516370a0823160e01b81526001600160a01b0387811660048301525f917f0000000000000000000000000000000000000000000000000000000000000000909116906370a0823190602401602060405180830381865afa1580156109f9573d5f803e3d5ffd5b505050506040513d601f19601f82011682018060405250810190610a1d91906120bb565b905084811015610a665760405162461bcd60e51b8152602060048201526014602482015273696e73756666696369656e742062616c616e636560601b60448201526064016108d9565b5f610a7185876112c7565b90505f6001600160a01b03861663a9059cbb89610a96670de0b6b3a76400008661207e565b6040516001600160e01b031960e085901b1681526001600160a01b03909216600483015260248201526044016020604051808303815f875af1158015610ade573d5f803e3d5ffd5b505050506040513d601f19601f82011682018060405250810190610b029190612095565b90508015610b8157610b1489886113de565b6001600160a01b03808a165f908152600360209081526040808320938a1683529290529081208054849290610b4a908490612040565b90915550506001600160a01b0389165f9081526004602052604081208054899290610b76908490612040565b90915550610b9a9050565b604051634dc34d1760e01b815260040160405180910390fd5b610ba389611a74565b505050610baf60015f55565b505050505050565b80805f03610bd85760405163cb1d3f7d60e01b815260040160405180910390fd5b610be0611a49565b610be86119f4565b6001600160a01b0383165f9081526004602052604081208054849290610c0f90849061206b565b90915550610c1e905083611a74565b5f6001600160a01b037f0000000000000000000000000000000000000000000000000000000000000000166340c10f1985610c61670de0b6b3a76400008761207e565b6040516001600160e01b031960e085901b1681526001600160a01b03909216600483015260248201526044016020604051808303815f875af1158015610ca9573d5f803e3d5ffd5b505050506040513d601f19601f82011682018060405250810190610ccd9190612095565b9050600181151514610cf2576040516330eb0fc960e01b815260040160405180910390fd5b600980546001810182555f919091527f6e1540171b6c0c960b71a7020d9f60077f6af931a8bbf590da0223dacf75c7af0180546001600160a01b0319166001600160a01b03861690811790915560408051918252602082018590527f2e626cdf1e4f103a37d55c73b033c27479722aab33daa0b13bfd94b9cb319dec910161081d565b610d7d611a1c565b610d865f611bfb565b565b5f80610d93836119cb565b91509150915091565b5f805b600854811015610e10575f60088281548110610dbd57610dbd612018565b5f9182526020808320909101546001600160a01b03878116845260038352604080852091909216808552925290912054909150610dfa8282611c4c565b610e04908561206b565b93505050600101610d9f565b50919050565b610e1e611a1c565b610e26611a49565b610d86611cf4565b60606009805480602002602001604051908101604052809291908181526020018280548015610e8457602002820191905f5260205f20905b81546001600160a01b03168152600190910190602001808311610e66575b5050505050905090565b82805f03610eaf5760405163cb1d3f7d60e01b815260040160405180910390fd5b610eb76119f4565b610ebf611a49565b6001600160a01b0383165f9081526007602052604081208054869290610ee690849061206b565b90915550506040516370a0823160e01b81526001600160a01b0384811660048301525f917f0000000000000000000000000000000000000000000000000000000000000000909116906370a0823190602401602060405180830381865afa158015610f53573d5f803e3d5ffd5b505050506040513d601f19601f82011682018060405250810190610f7791906120bb565b905084811015610fc05760405162461bcd60e51b8152602060048201526014602482015273696e73756666696369656e742062616c616e636560601b60448201526064016108d9565b5f6001600160a01b037f0000000000000000000000000000000000000000000000000000000000000000166323b872dd8686611004670de0b6b3a76400008b61207e565b6040518463ffffffff1660e01b8152600401611022939291906120d2565b6020604051808303815f875af115801561103e573d5f803e3d5ffd5b505050506040513d601f19601f820116820180604052508101906110629190612095565b90508061108257604051634dc34d1760e01b815260040160405180910390fd5b7f21141798e799a89effd25c61078f7d75d4bdc3a58aa15da24f720f282983c8268585886040516110b5939291906120d2565b60405180910390a1505061091660015f55565b80805f036110e95760405163cb1d3f7d60e01b815260040160405180910390fd5b6110f16119f4565b6110f9611a49565b5f61110385611d54565b9050670de0b6b3a7640000811061112d5760405163032c072f60e41b815260040160405180910390fd5b5f61113887856112c7565b90505f6064611148600a8461207e565b61115291906120f6565b905061116888611162838561206b565b88611aac565b61117286866113de565b6001600160a01b0386165f9081526004602052604081208054879290611199908490612040565b909155506111a99050818361206b565b6001600160a01b038089165f908152600360209081526040808320938d16835292905290812080549091906111df908490612040565b909155505f90506111ef88611d54565b9050838111611211576040516303e141b160e51b815260040160405180910390fd5b7fef98c08a4271ff4ed68b4bc1ed60195a8805fb078d5e09a200a4c8b439309c5e888888604051611244939291906120d2565b60405180910390a161125587611a74565b5050505061126260015f55565b5050505050565b6060600a805480602002602001604051908101604052809291908181526020018280548015610e8457602002820191905f5260205f209081546001600160a01b03168152600190910190602001808311610e66575050505050905090565b6001600160a01b038083165f90815260026020526040808220548151633fabe5a360e21b81529151929316918391839163feaf968c9160048082019260a0929091908290030181865afa158015611320573d5f803e3d5ffd5b505050506040513d601f19601f82011682018060405250810190611344919061212e565b5050509150506402540be4008161135b919061207e565b61136d670de0b6b3a76400008661207e565b61137791906120f6565b95945050505050565b60606008805480602002602001604051908101604052809291908181526020018280548015610e8457602002820191905f5260205f209081546001600160a01b03168152600190910190602001808311610e66575050505050905090565b6113e66119f4565b80805f036114075760405163cb1d3f7d60e01b815260040160405180910390fd5b6001600160a01b0383165f908152600660205260408120805484929061142e90849061206b565b90915550506001600160a01b0383165f908152600460205260408120805484929061145a908490612040565b909155505f90506001600160a01b037f000000000000000000000000000000000000000000000000000000000000000016639dc29fac856114a3670de0b6b3a76400008761207e565b6040516001600160e01b031960e085901b1681526001600160a01b03909216600483015260248201526044016020604051808303815f875af11580156114eb573d5f803e3d5ffd5b505050506040513d601f19601f8201168201806040525081019061150f9190612095565b90508061152f57604051634dc34d1760e01b815260040160405180910390fd5b604080516001600160a01b0386168152602081018590527ef562d816d96d45497f40008d705b794f2892a5bba96580a3886989c487c7bf910160405180910390a1505061157b60015f55565b5050565b6115876119f4565b61158f611a49565b5f61159983611766565b9050828110156116035760405162461bcd60e51b815260206004820152602f60248201527f6465706f7369742031353025206f66207468652044534320616d6f756e74207960448201526e1bdd481dd85b9d081d1bc81b5a5b9d608a1b60648201526084016108d9565b5f61160e85836112c7565b905061161b8584836117d3565b6116258385610bb7565b505061082f60015f55565b6040516370a0823160e01b81526001600160a01b0382811660048301525f9182917f000000000000000000000000000000000000000000000000000000000000000016906370a0823190602401602060405180830381865afa158015611698573d5f803e3d5ffd5b505050506040513d601f19601f820116820180604052508101906116bc91906120bb565b1192915050565b5f6105d98383611c4c565b81805f036116ef5760405163cb1d3f7d60e01b815260040160405180910390fd5b6001600160a01b038085165f908152600260205260409020548591166117335760405163cf39359560e01b81526001600160a01b03821660048201526024016108d9565b61173b611a49565b61174583856113de565b5f61175086866112c7565b905061175d868286611aac565b610baf84611a74565b5f606461177460328461207e565b6105dc91906120f6565b611786611a1c565b6001600160a01b0381166117af57604051631e4fbdf760e01b81525f60048201526024016108d9565b6117b881611bfb565b50565b6117c3611a1c565b6117cb611d6e565b610d86611d98565b80805f036117f45760405163cb1d3f7d60e01b815260040160405180910390fd5b6117fc6119f4565b6001600160a01b038085165f908152600260205260409020548591166118405760405163cf39359560e01b81526001600160a01b03821660048201526024016108d9565b5f61184b86856112c7565b90505f6001600160a01b0387166323b872dd8730611871670de0b6b3a76400008761207e565b6040518463ffffffff1660e01b815260040161188f939291906120d2565b6020604051808303815f875af11580156118ab573d5f803e3d5ffd5b505050506040513d601f19601f820116820180604052508101906118cf9190612095565b9050806118ef57604051634dc34d1760e01b815260040160405180910390fd5b6001600160a01b038087165f908152600360209081526040808320938b168352929052908120805487929061192590849061206b565b90915550506040517ff1c0dd7e9b98bbff859029005ef89b127af049cd18df1a8d79f0b7e019911e569061195e9088908a9089906120d2565b60405180910390a150505061091660015f55565b5f6105dc82611d54565b5f825f0361198c57505f196105dc565b5f606461199a60328561207e565b6119a491906120f6565b9050836119b9670de0b6b3a76400008361207e565b6119c391906120f6565b949350505050565b6001600160a01b0381165f90815260046020526040812054906119ed83610d9c565b9050915091565b60025f5403611a1657604051633ee5aeb560e01b815260040160405180910390fd5b60025f55565b6001546001600160a01b03163314610d865760405163118cdaa760e01b81523360048201526024016108d9565b600154600160a01b900460ff1615610d865760405163d93c066560e01b815260040160405180910390fd5b5f611a7e82611d54565b9050670de0b6b3a764000081101561157b5760405163e580cc6160e01b8152600481018290526024016108d9565b611ab46119f4565b81805f03611ad55760405163cb1d3f7d60e01b815260040160405180910390fd5b6001600160a01b038083165f90815260036020908152604080832093881683529290529081208054859290611b0b908490612040565b90915550506040517ff33cf7e8426ed717e8a8ced86566df3640aa079135bc17e05fe2afa6cbf029ab90611b44908490879087906120d2565b60405180910390a15f6001600160a01b03851663a9059cbb84611b6f670de0b6b3a76400008861207e565b6040516001600160e01b031960e085901b1681526001600160a01b03909216600483015260248201526044016020604051808303815f875af1158015611bb7573d5f803e3d5ffd5b505050506040513d601f19601f82011682018060405250810190611bdb9190612095565b90508061162557604051634dc34d1760e01b815260040160405180910390fd5b600180546001600160a01b038381166001600160a01b0319831681179093556040519116919082907f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e0905f90a35050565b6001600160a01b038083165f90815260026020526040808220548151633fabe5a360e21b81529151929316918391839163feaf968c9160048082019260a0929091908290030181865afa158015611ca5573d5f803e3d5ffd5b505050506040513d601f19601f82011682018060405250810190611cc9919061212e565b505050915050670de0b6b3a7640000846402540be40083611cea919061207e565b61136d919061207e565b611cfc611a49565b6001805460ff60a01b1916600160a01b1790557f62e78cea01bee320cd4e420270b5ea74000d11b0c9f74754ebdbfc544b05a258611d373390565b6040516001600160a01b03909116815260200160405180910390a1565b5f805f611d60846119cb565b915091506119c3828261197c565b600154600160a01b900460ff16610d8657604051638dfc202b60e01b815260040160405180910390fd5b611da0611d6e565b6001805460ff60a01b191690557f5db9ee0a495bf2e6ff9c91a7834c1ba4fdd244a5e8aa4e537bd38aeae4b073aa33611d37565b5f8060408385031215611de5575f80fd5b50508035926020909101359150565b80356001600160a01b0381168114611e0a575f80fd5b919050565b5f60208284031215611e1f575f80fd5b6105d982611df4565b5f8060408385031215611e39575f80fd5b611e4283611df4565b9150611e5060208401611df4565b90509250929050565b5f8060408385031215611e6a575f80fd5b611e7383611df4565b946020939093013593505050565b5f805f60608486031215611e93575f80fd5b611e9c84611df4565b925060208401359150611eb160408501611df4565b90509250925092565b5f805f8060808587031215611ecd575f80fd5b611ed685611df4565b9350611ee460208601611df4565b925060408501359150611ef960608601611df4565b905092959194509250565b602080825282518282018190525f918401906040840190835b81811015611f445783516001600160a01b0316835260209384019390920191600101611f1d565b509095945050505050565b5f805f60608486031215611f61575f80fd5b83359250611f7160208501611df4565b9150611eb160408501611df4565b5f805f8060808587031215611f92575f80fd5b611f9b85611df4565b9350611fa960208601611df4565b9250611fb760408601611df4565b9396929550929360600135925050565b5f60208284031215611fd7575f80fd5b5035919050565b5f805f60608486031215611ff0575f80fd5b611ff984611df4565b925061200760208501611df4565b929592945050506040919091013590565b634e487b7160e01b5f52603260045260245ffd5b634e487b7160e01b5f52601160045260245ffd5b818103818111156105dc576105dc61202c565b5f600182016120645761206461202c565b5060010190565b808201808211156105dc576105dc61202c565b80820281158282048414176105dc576105dc61202c565b5f602082840312156120a5575f80fd5b815180151581146120b4575f80fd5b9392505050565b5f602082840312156120cb575f80fd5b5051919050565b6001600160a01b039384168152919092166020820152604081019190915260600190565b5f8261211057634e487b7160e01b5f52601260045260245ffd5b500490565b805169ffffffffffffffffffff81168114611e0a575f80fd5b5f805f805f60a08688031215612142575f80fd5b61214b86612115565b6020870151604088015160608901519297509095509350915061217060808701612115565b9050929550929590935056fea26469706673582212201d4a181bed8214323aeea056daa2e63e8057e10b75db0bc3037b9729e011c86664736f6c634300081a0033";

    public static final String FUNC__BURNDSC = "_burnDsc";

    public static final String FUNC_BURNDSC = "burnDsc";

    public static final String FUNC_CALCULATE150PERCENTOFCOLLATERALAMOUNTTOMINT = "calculate150PercentOfCollateralAmountToMint";

    public static final String FUNC_CALCULATEHEALTHFACTOR = "calculateHealthFactor";

    public static final String FUNC_DEPOSITCOLLATERAL = "depositCollateral";

    public static final String FUNC_DEPOSITCOLLATERALANDMINTDSC = "depositCollateralAndMintDsc";

    public static final String FUNC_GETACCOUNTCOLLATERALVALUE = "getAccountCollateralValue";

    public static final String FUNC_GETACCOUNTINFORMATION = "getAccountInformation";

    public static final String FUNC_GETADDITIONALFEEDPRECISION = "getAdditionalFeedPrecision";

    public static final String FUNC_GETALLUSERSTHROUGHBANKAPPLICATION = "getAllUsersThroughBankApplication";

    public static final String FUNC_GETALLUSERSTHROUGHCOLLATERALIZATION = "getAllUsersThroughCollateralization";

    public static final String FUNC_GETCOLLATERALBALANCEOFUSER = "getCollateralBalanceOfUser";

    public static final String FUNC_GETCOLLATERALTOKENPRICEFEED = "getCollateralTokenPriceFeed";

    public static final String FUNC_GETCOLLATERALTOKENS = "getCollateralTokens";

    public static final String FUNC_GETDSC = "getDsc";

    public static final String FUNC_GETHEALTHFACTOR = "getHealthFactor";

    public static final String FUNC_GETLIQUIDATIONBONUS = "getLiquidationBonus";

    public static final String FUNC_GETLIQUIDATIONPRECISION = "getLiquidationPrecision";

    public static final String FUNC_GETLIQUIDATIONTHRESHOLD = "getLiquidationThreshold";

    public static final String FUNC_GETMINHEALTHFACTOR = "getMinHealthFactor";

    public static final String FUNC_GETPRECISION = "getPrecision";

    public static final String FUNC_GETTOKENAMOUNTFROMUSD = "getTokenAmountFromUsd";

    public static final String FUNC_GETTOTALTOKENSBURNT = "getTotalTokensBurnt";

    public static final String FUNC_GETTOTALTOKENSMINTEDTOUSER = "getTotalTokensMintedToUser";

    public static final String FUNC_GETTOTALTOKENSTRANSFERREDBYUSER = "getTotalTokensTransferredByUser";

    public static final String FUNC_GETUNDERCOLLATERALIZEDUSERSANDTHEIRDEBT = "getUnderCollateralizedUsersAndTheirDebt";

    public static final String FUNC_GETUSDVALUE = "getUsdValue";

    public static final String FUNC_ISVALIDTOKEN = "isValidToken";

    public static final String FUNC_LIQUIDATE = "liquidate";

    public static final String FUNC_MINTDSC = "mintDsc";

    public static final String FUNC_MINTDSCFORBANK = "mintDscForBank";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_PAUSE = "pause";

    public static final String FUNC_PAUSED = "paused";

    public static final String FUNC_REDEEMCOLLATERAL = "redeemCollateral";

    public static final String FUNC_REDEEMCOLLATERALFORDSC = "redeemCollateralForDsc";

    public static final String FUNC_RENOUNCEOWNERSHIP = "renounceOwnership";

    public static final String FUNC_TRANSFERCOLLATERALTOBANKADDRESSANDBURNEQUIVALENTDSC = "transferCollateralToBankAddressAndBurnEquivalentDSc";

    public static final String FUNC_TRANSFERDSCFROMUSER1TOUSER2 = "transferDscFromUser1ToUser2";

    public static final String FUNC_TRANSFEROWNERSHIP = "transferOwnership";

    public static final String FUNC_UNPAUSE = "unPause";

    public static final Event BANKTOKENMINTEVENT_EVENT = new Event("BankTokenMintEvent", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event COLLATERALDEPOSITED_EVENT = new Event("CollateralDeposited", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event COLLATERALDEPOSITEDBYBANKEVENT_EVENT = new Event("CollateralDepositedByBankEvent", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event COLLATERALREDEEMEDEVENT_EVENT = new Event("CollateralRedeemedEvent", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event LIQUIDATIONEVENT_EVENT = new Event("LiquidationEvent", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event OWNERSHIPTRANSFERRED_EVENT = new Event("OwnershipTransferred", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}));
    ;

    public static final Event PAUSED_EVENT = new Event("Paused", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
    ;

    public static final Event TOKENBURNEVENT_EVENT = new Event("TokenBurnEvent", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event TOKENMINTEVENT_EVENT = new Event("TokenMintEvent", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event TOKENTRANSFEREVENT_EVENT = new Event("TokenTransferEvent", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event UNPAUSED_EVENT = new Event("Unpaused", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
    ;

    @Deprecated
    protected DSCEngine(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected DSCEngine(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected DSCEngine(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected DSCEngine(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static List<BankTokenMintEventEventResponse> getBankTokenMintEventEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = Collections.singletonList(staticExtractEventParametersWithLog(BANKTOKENMINTEVENT_EVENT, (Log) transactionReceipt.getLogs()));
        ArrayList<BankTokenMintEventEventResponse> responses = new ArrayList<BankTokenMintEventEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            BankTokenMintEventEventResponse typedResponse = new BankTokenMintEventEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.to = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static BankTokenMintEventEventResponse getBankTokenMintEventEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(BANKTOKENMINTEVENT_EVENT, log);
        BankTokenMintEventEventResponse typedResponse = new BankTokenMintEventEventResponse();
        typedResponse.log = log;
        typedResponse.to = (String) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
        return typedResponse;
    }

    public Flowable<BankTokenMintEventEventResponse> bankTokenMintEventEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getBankTokenMintEventEventFromLog(log));
    }

    public Flowable<BankTokenMintEventEventResponse> bankTokenMintEventEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(BANKTOKENMINTEVENT_EVENT));
        return bankTokenMintEventEventFlowable(filter);
    }

    public static List<CollateralDepositedEventResponse> getCollateralDepositedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = Collections.singletonList(staticExtractEventParametersWithLog(COLLATERALDEPOSITED_EVENT, (Log) transactionReceipt.getLogs()));
        ArrayList<CollateralDepositedEventResponse> responses = new ArrayList<CollateralDepositedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            CollateralDepositedEventResponse typedResponse = new CollateralDepositedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.user = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.token = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static CollateralDepositedEventResponse getCollateralDepositedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(COLLATERALDEPOSITED_EVENT, log);
        CollateralDepositedEventResponse typedResponse = new CollateralDepositedEventResponse();
        typedResponse.log = log;
        typedResponse.user = (String) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.token = (String) eventValues.getNonIndexedValues().get(1).getValue();
        typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
        return typedResponse;
    }

    public Flowable<CollateralDepositedEventResponse> collateralDepositedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getCollateralDepositedEventFromLog(log));
    }

    public Flowable<CollateralDepositedEventResponse> collateralDepositedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(COLLATERALDEPOSITED_EVENT));
        return collateralDepositedEventFlowable(filter);
    }

    public static List<CollateralDepositedByBankEventEventResponse> getCollateralDepositedByBankEventEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = Collections.singletonList(staticExtractEventParametersWithLog(COLLATERALDEPOSITEDBYBANKEVENT_EVENT, (Log) transactionReceipt.getLogs()));
        ArrayList<CollateralDepositedByBankEventEventResponse> responses = new ArrayList<CollateralDepositedByBankEventEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            CollateralDepositedByBankEventEventResponse typedResponse = new CollateralDepositedByBankEventEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.user = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.token = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static CollateralDepositedByBankEventEventResponse getCollateralDepositedByBankEventEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(COLLATERALDEPOSITEDBYBANKEVENT_EVENT, log);
        CollateralDepositedByBankEventEventResponse typedResponse = new CollateralDepositedByBankEventEventResponse();
        typedResponse.log = log;
        typedResponse.user = (String) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.token = (String) eventValues.getNonIndexedValues().get(1).getValue();
        typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
        return typedResponse;
    }

    public Flowable<CollateralDepositedByBankEventEventResponse> collateralDepositedByBankEventEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getCollateralDepositedByBankEventEventFromLog(log));
    }

    public Flowable<CollateralDepositedByBankEventEventResponse> collateralDepositedByBankEventEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(COLLATERALDEPOSITEDBYBANKEVENT_EVENT));
        return collateralDepositedByBankEventEventFlowable(filter);
    }

    public static List<CollateralRedeemedEventEventResponse> getCollateralRedeemedEventEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = Collections.singletonList(staticExtractEventParametersWithLog(COLLATERALREDEEMEDEVENT_EVENT, (Log) transactionReceipt.getLogs()));
        ArrayList<CollateralRedeemedEventEventResponse> responses = new ArrayList<CollateralRedeemedEventEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            CollateralRedeemedEventEventResponse typedResponse = new CollateralRedeemedEventEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.redeemTo = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.token = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static CollateralRedeemedEventEventResponse getCollateralRedeemedEventEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(COLLATERALREDEEMEDEVENT_EVENT, log);
        CollateralRedeemedEventEventResponse typedResponse = new CollateralRedeemedEventEventResponse();
        typedResponse.log = log;
        typedResponse.redeemTo = (String) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.token = (String) eventValues.getNonIndexedValues().get(1).getValue();
        typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
        return typedResponse;
    }

    public Flowable<CollateralRedeemedEventEventResponse> collateralRedeemedEventEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getCollateralRedeemedEventEventFromLog(log));
    }

    public Flowable<CollateralRedeemedEventEventResponse> collateralRedeemedEventEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(COLLATERALREDEEMEDEVENT_EVENT));
        return collateralRedeemedEventEventFlowable(filter);
    }

    public static List<LiquidationEventEventResponse> getLiquidationEventEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = Collections.singletonList(staticExtractEventParametersWithLog(LIQUIDATIONEVENT_EVENT, (Log) transactionReceipt.getLogs()));
        ArrayList<LiquidationEventEventResponse> responses = new ArrayList<LiquidationEventEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            LiquidationEventEventResponse typedResponse = new LiquidationEventEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.user = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.liquidator = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.debtToCover = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static LiquidationEventEventResponse getLiquidationEventEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(LIQUIDATIONEVENT_EVENT, log);
        LiquidationEventEventResponse typedResponse = new LiquidationEventEventResponse();
        typedResponse.log = log;
        typedResponse.user = (String) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.liquidator = (String) eventValues.getNonIndexedValues().get(1).getValue();
        typedResponse.debtToCover = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
        return typedResponse;
    }

    public Flowable<LiquidationEventEventResponse> liquidationEventEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getLiquidationEventEventFromLog(log));
    }

    public Flowable<LiquidationEventEventResponse> liquidationEventEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(LIQUIDATIONEVENT_EVENT));
        return liquidationEventEventFlowable(filter);
    }

    public static List<OwnershipTransferredEventResponse> getOwnershipTransferredEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = Collections.singletonList(staticExtractEventParametersWithLog(OWNERSHIPTRANSFERRED_EVENT, (Log) transactionReceipt.getLogs()));
        ArrayList<OwnershipTransferredEventResponse> responses = new ArrayList<OwnershipTransferredEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            OwnershipTransferredEventResponse typedResponse = new OwnershipTransferredEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.previousOwner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.newOwner = (String) eventValues.getIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static OwnershipTransferredEventResponse getOwnershipTransferredEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(OWNERSHIPTRANSFERRED_EVENT, log);
        OwnershipTransferredEventResponse typedResponse = new OwnershipTransferredEventResponse();
        typedResponse.log = log;
        typedResponse.previousOwner = (String) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.newOwner = (String) eventValues.getIndexedValues().get(1).getValue();
        return typedResponse;
    }

    public Flowable<OwnershipTransferredEventResponse> ownershipTransferredEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getOwnershipTransferredEventFromLog(log));
    }

    public Flowable<OwnershipTransferredEventResponse> ownershipTransferredEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(OWNERSHIPTRANSFERRED_EVENT));
        return ownershipTransferredEventFlowable(filter);
    }

    public static List<PausedEventResponse> getPausedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = Collections.singletonList(staticExtractEventParametersWithLog(PAUSED_EVENT, (Log) transactionReceipt.getLogs()));
        ArrayList<PausedEventResponse> responses = new ArrayList<PausedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            PausedEventResponse typedResponse = new PausedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.account = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static PausedEventResponse getPausedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(PAUSED_EVENT, log);
        PausedEventResponse typedResponse = new PausedEventResponse();
        typedResponse.log = log;
        typedResponse.account = (String) eventValues.getNonIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<PausedEventResponse> pausedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getPausedEventFromLog(log));
    }

    public Flowable<PausedEventResponse> pausedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(PAUSED_EVENT));
        return pausedEventFlowable(filter);
    }

    public static List<TokenBurnEventEventResponse> getTokenBurnEventEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = Collections.singletonList(staticExtractEventParametersWithLog(TOKENBURNEVENT_EVENT, (Log) transactionReceipt.getLogs()));
        ArrayList<TokenBurnEventEventResponse> responses = new ArrayList<TokenBurnEventEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            TokenBurnEventEventResponse typedResponse = new TokenBurnEventEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.from = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static TokenBurnEventEventResponse getTokenBurnEventEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(TOKENBURNEVENT_EVENT, log);
        TokenBurnEventEventResponse typedResponse = new TokenBurnEventEventResponse();
        typedResponse.log = log;
        typedResponse.from = (String) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
        return typedResponse;
    }

    public Flowable<TokenBurnEventEventResponse> tokenBurnEventEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getTokenBurnEventEventFromLog(log));
    }

    public Flowable<TokenBurnEventEventResponse> tokenBurnEventEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(TOKENBURNEVENT_EVENT));
        return tokenBurnEventEventFlowable(filter);
    }

    public static List<TokenMintEventEventResponse> getTokenMintEventEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = Collections.singletonList(staticExtractEventParametersWithLog(TOKENMINTEVENT_EVENT, (Log) transactionReceipt.getLogs()));
        ArrayList<TokenMintEventEventResponse> responses = new ArrayList<TokenMintEventEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            TokenMintEventEventResponse typedResponse = new TokenMintEventEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.to = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static TokenMintEventEventResponse getTokenMintEventEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(TOKENMINTEVENT_EVENT, log);
        TokenMintEventEventResponse typedResponse = new TokenMintEventEventResponse();
        typedResponse.log = log;
        typedResponse.to = (String) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
        return typedResponse;
    }

    public Flowable<TokenMintEventEventResponse> tokenMintEventEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getTokenMintEventEventFromLog(log));
    }

    public Flowable<TokenMintEventEventResponse> tokenMintEventEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(TOKENMINTEVENT_EVENT));
        return tokenMintEventEventFlowable(filter);
    }

    public static List<TokenTransferEventEventResponse> getTokenTransferEventEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = Collections.singletonList(staticExtractEventParametersWithLog(TOKENTRANSFEREVENT_EVENT, (Log) transactionReceipt.getLogs()));
        ArrayList<TokenTransferEventEventResponse> responses = new ArrayList<TokenTransferEventEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            TokenTransferEventEventResponse typedResponse = new TokenTransferEventEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.from = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.to = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static TokenTransferEventEventResponse getTokenTransferEventEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(TOKENTRANSFEREVENT_EVENT, log);
        TokenTransferEventEventResponse typedResponse = new TokenTransferEventEventResponse();
        typedResponse.log = log;
        typedResponse.from = (String) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.to = (String) eventValues.getNonIndexedValues().get(1).getValue();
        typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
        return typedResponse;
    }

    public Flowable<TokenTransferEventEventResponse> tokenTransferEventEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getTokenTransferEventEventFromLog(log));
    }

    public Flowable<TokenTransferEventEventResponse> tokenTransferEventEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(TOKENTRANSFEREVENT_EVENT));
        return tokenTransferEventEventFlowable(filter);
    }

    public static List<UnpausedEventResponse> getUnpausedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = Collections.singletonList(staticExtractEventParametersWithLog(UNPAUSED_EVENT, (Log) transactionReceipt.getLogs()));
        ArrayList<UnpausedEventResponse> responses = new ArrayList<UnpausedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            UnpausedEventResponse typedResponse = new UnpausedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.account = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static UnpausedEventResponse getUnpausedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(UNPAUSED_EVENT, log);
        UnpausedEventResponse typedResponse = new UnpausedEventResponse();
        typedResponse.log = log;
        typedResponse.account = (String) eventValues.getNonIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<UnpausedEventResponse> unpausedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getUnpausedEventFromLog(log));
    }

    public Flowable<UnpausedEventResponse> unpausedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(UNPAUSED_EVENT));
        return unpausedEventFlowable(filter);
    }

    public RemoteFunctionCall<TransactionReceipt> _burnDsc(String from, BigInteger amount) {
        final Function function = new Function(
                FUNC__BURNDSC, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, from), 
                new org.web3j.abi.datatypes.generated.Uint256(amount)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> burnDsc(String user, BigInteger amount) {
        final Function function = new Function(
                FUNC_BURNDSC, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, user), 
                new org.web3j.abi.datatypes.generated.Uint256(amount)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> calculate150PercentOfCollateralAmountToMint(BigInteger amountDscToMintInUsd) {
        final Function function = new Function(FUNC_CALCULATE150PERCENTOFCOLLATERALAMOUNTTOMINT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(amountDscToMintInUsd)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> calculateHealthFactor(BigInteger totalDscMinted, BigInteger collateralValueInUsd) {
        final Function function = new Function(FUNC_CALCULATEHEALTHFACTOR, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(totalDscMinted), 
                new org.web3j.abi.datatypes.generated.Uint256(collateralValueInUsd)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> depositCollateral(String tokenCollateralAddress, String user, BigInteger amountCollateralInUSD) {
        final Function function = new Function(
                FUNC_DEPOSITCOLLATERAL, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, tokenCollateralAddress), 
                new org.web3j.abi.datatypes.Address(160, user), 
                new org.web3j.abi.datatypes.generated.Uint256(amountCollateralInUSD)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> depositCollateralAndMintDsc(String tokenCollateralAddress, BigInteger amountDscToMintInUSD, String user) {
        final Function function = new Function(
                FUNC_DEPOSITCOLLATERALANDMINTDSC, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, tokenCollateralAddress), 
                new org.web3j.abi.datatypes.generated.Uint256(amountDscToMintInUSD), 
                new org.web3j.abi.datatypes.Address(160, user)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> getAccountCollateralValue(String user) {
        final Function function = new Function(FUNC_GETACCOUNTCOLLATERALVALUE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, user)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<Tuple2<BigInteger, BigInteger>> getAccountInformation(String user) {
        final Function function = new Function(FUNC_GETACCOUNTINFORMATION, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, user)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        return new RemoteFunctionCall<Tuple2<BigInteger, BigInteger>>(function,
                new Callable<Tuple2<BigInteger, BigInteger>>() {
                    @Override
                    public Tuple2<BigInteger, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple2<BigInteger, BigInteger>(
                                (BigInteger) results.get(0).getValue(), 
                                (BigInteger) results.get(1).getValue());
                    }
                });
    }

    public RemoteFunctionCall<BigInteger> getAdditionalFeedPrecision() {
        final Function function = new Function(FUNC_GETADDITIONALFEEDPRECISION, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<List> getAllUsersThroughBankApplication() {
        final Function function = new Function(FUNC_GETALLUSERSTHROUGHBANKAPPLICATION, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Address>>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteFunctionCall<List> getAllUsersThroughCollateralization() {
        final Function function = new Function(FUNC_GETALLUSERSTHROUGHCOLLATERALIZATION, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Address>>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteFunctionCall<BigInteger> getCollateralBalanceOfUser(String user, String token) {
        final Function function = new Function(FUNC_GETCOLLATERALBALANCEOFUSER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, user), 
                new org.web3j.abi.datatypes.Address(160, token)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<String> getCollateralTokenPriceFeed(String token) {
        final Function function = new Function(FUNC_GETCOLLATERALTOKENPRICEFEED, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, token)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<List> getCollateralTokens() {
        final Function function = new Function(FUNC_GETCOLLATERALTOKENS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Address>>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteFunctionCall<String> getDsc() {
        final Function function = new Function(FUNC_GETDSC, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<BigInteger> getHealthFactor(String user) {
        final Function function = new Function(FUNC_GETHEALTHFACTOR, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, user)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> getLiquidationBonus() {
        final Function function = new Function(FUNC_GETLIQUIDATIONBONUS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> getLiquidationPrecision() {
        final Function function = new Function(FUNC_GETLIQUIDATIONPRECISION, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> getLiquidationThreshold() {
        final Function function = new Function(FUNC_GETLIQUIDATIONTHRESHOLD, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> getMinHealthFactor() {
        final Function function = new Function(FUNC_GETMINHEALTHFACTOR, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> getPrecision() {
        final Function function = new Function(FUNC_GETPRECISION, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> getTokenAmountFromUsd(String token, BigInteger usdAmountInWei) {
        final Function function = new Function(FUNC_GETTOKENAMOUNTFROMUSD, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, token), 
                new org.web3j.abi.datatypes.generated.Uint256(usdAmountInWei)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> getTotalTokensBurnt(String user) {
        final Function function = new Function(FUNC_GETTOTALTOKENSBURNT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, user)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> getTotalTokensMintedToUser(String user) {
        final Function function = new Function(FUNC_GETTOTALTOKENSMINTEDTOUSER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, user)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> getTotalTokensTransferredByUser(String user) {
        final Function function = new Function(FUNC_GETTOTALTOKENSTRANSFERREDBYUSER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, user)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<Tuple2<String, BigInteger>> getUnderCollateralizedUsersAndTheirDebt() {
        final Function function = new Function(FUNC_GETUNDERCOLLATERALIZEDUSERSANDTHEIRDEBT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
        return new RemoteFunctionCall<Tuple2<String, BigInteger>>(function,
                new Callable<Tuple2<String, BigInteger>>() {
                    @Override
                    public Tuple2<String, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple2<String, BigInteger>(
                                (String) results.get(0).getValue(), 
                                (BigInteger) results.get(1).getValue());
                    }
                });
    }

    public RemoteFunctionCall<BigInteger> getUsdValue(String token, BigInteger amount) {
        final Function function = new Function(FUNC_GETUSDVALUE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, token), 
                new org.web3j.abi.datatypes.generated.Uint256(amount)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<Boolean> isValidToken(String user) {
        final Function function = new Function(FUNC_ISVALIDTOKEN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, user)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<TransactionReceipt> liquidate(String collateral, String user, String liquidator, BigInteger debtToCover) {
        final Function function = new Function(
                FUNC_LIQUIDATE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, collateral), 
                new org.web3j.abi.datatypes.Address(160, user), 
                new org.web3j.abi.datatypes.Address(160, liquidator), 
                new org.web3j.abi.datatypes.generated.Uint256(debtToCover)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> mintDsc(String to, BigInteger amountDscToMint) {
        final Function function = new Function(
                FUNC_MINTDSC, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, to), 
                new org.web3j.abi.datatypes.generated.Uint256(amountDscToMint)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> mintDscForBank(String to, BigInteger amountDSC) {
        final Function function = new Function(
                FUNC_MINTDSCFORBANK, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, to), 
                new org.web3j.abi.datatypes.generated.Uint256(amountDSC)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> owner() {
        final Function function = new Function(FUNC_OWNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> pause() {
        final Function function = new Function(
                FUNC_PAUSE, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> paused() {
        final Function function = new Function(FUNC_PAUSED, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<TransactionReceipt> redeemCollateral(String tokenCollateralAddress, BigInteger amountCollateralInUSD, String user) {
        final Function function = new Function(
                FUNC_REDEEMCOLLATERAL, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, tokenCollateralAddress), 
                new org.web3j.abi.datatypes.generated.Uint256(amountCollateralInUSD), 
                new org.web3j.abi.datatypes.Address(160, user)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> redeemCollateralForDsc(String tokenCollateralAddress, BigInteger amountDscInUSD, String user) {
        final Function function = new Function(
                FUNC_REDEEMCOLLATERALFORDSC, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, tokenCollateralAddress), 
                new org.web3j.abi.datatypes.generated.Uint256(amountDscInUSD), 
                new org.web3j.abi.datatypes.Address(160, user)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> renounceOwnership() {
        final Function function = new Function(
                FUNC_RENOUNCEOWNERSHIP, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> transferCollateralToBankAddressAndBurnEquivalentDSc(String user, String bankAddress, BigInteger amountToConvertToCash, String collateralAddress) {
        final Function function = new Function(
                FUNC_TRANSFERCOLLATERALTOBANKADDRESSANDBURNEQUIVALENTDSC, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, user), 
                new org.web3j.abi.datatypes.Address(160, bankAddress), 
                new org.web3j.abi.datatypes.generated.Uint256(amountToConvertToCash), 
                new org.web3j.abi.datatypes.Address(160, collateralAddress)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> transferDscFromUser1ToUser2(BigInteger amount, String from, String to) {
        final Function function = new Function(
                FUNC_TRANSFERDSCFROMUSER1TOUSER2, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(amount), 
                new org.web3j.abi.datatypes.Address(160, from), 
                new org.web3j.abi.datatypes.Address(160, to)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> transferOwnership(String newOwner) {
        final Function function = new Function(
                FUNC_TRANSFEROWNERSHIP, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, newOwner)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> unPause() {
        final Function function = new Function(
                FUNC_UNPAUSE, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static DSCEngine load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new DSCEngine(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static DSCEngine load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new DSCEngine(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static DSCEngine load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new DSCEngine(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static DSCEngine load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new DSCEngine(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<DSCEngine> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, List<String> tokenAddresses, List<String> priceFeedAddresses, String dscAddress) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>(
                        org.web3j.abi.datatypes.Address.class,
                        org.web3j.abi.Utils.typeMap(tokenAddresses, org.web3j.abi.datatypes.Address.class)), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>(
                        org.web3j.abi.datatypes.Address.class,
                        org.web3j.abi.Utils.typeMap(priceFeedAddresses, org.web3j.abi.datatypes.Address.class)), 
                new org.web3j.abi.datatypes.Address(160, dscAddress)));
        return deployRemoteCall(DSCEngine.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<DSCEngine> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, List<String> tokenAddresses, List<String> priceFeedAddresses, String dscAddress) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>(
                        org.web3j.abi.datatypes.Address.class,
                        org.web3j.abi.Utils.typeMap(tokenAddresses, org.web3j.abi.datatypes.Address.class)), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>(
                        org.web3j.abi.datatypes.Address.class,
                        org.web3j.abi.Utils.typeMap(priceFeedAddresses, org.web3j.abi.datatypes.Address.class)), 
                new org.web3j.abi.datatypes.Address(160, dscAddress)));
        return deployRemoteCall(DSCEngine.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<DSCEngine> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, List<String> tokenAddresses, List<String> priceFeedAddresses, String dscAddress) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>(
                        org.web3j.abi.datatypes.Address.class,
                        org.web3j.abi.Utils.typeMap(tokenAddresses, org.web3j.abi.datatypes.Address.class)), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>(
                        org.web3j.abi.datatypes.Address.class,
                        org.web3j.abi.Utils.typeMap(priceFeedAddresses, org.web3j.abi.datatypes.Address.class)), 
                new org.web3j.abi.datatypes.Address(160, dscAddress)));
        return deployRemoteCall(DSCEngine.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<DSCEngine> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, List<String> tokenAddresses, List<String> priceFeedAddresses, String dscAddress) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>(
                        org.web3j.abi.datatypes.Address.class,
                        org.web3j.abi.Utils.typeMap(tokenAddresses, org.web3j.abi.datatypes.Address.class)), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>(
                        org.web3j.abi.datatypes.Address.class,
                        org.web3j.abi.Utils.typeMap(priceFeedAddresses, org.web3j.abi.datatypes.Address.class)), 
                new org.web3j.abi.datatypes.Address(160, dscAddress)));
        return deployRemoteCall(DSCEngine.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static class BankTokenMintEventEventResponse extends BaseEventResponse {
        public String to;

        public BigInteger amount;
    }

    public static class CollateralDepositedEventResponse extends BaseEventResponse {
        public String user;

        public String token;

        public BigInteger amount;
    }

    public static class CollateralDepositedByBankEventEventResponse extends BaseEventResponse {
        public String user;

        public String token;

        public BigInteger amount;
    }

    public static class CollateralRedeemedEventEventResponse extends BaseEventResponse {
        public String redeemTo;

        public String token;

        public BigInteger amount;
    }

    public static class LiquidationEventEventResponse extends BaseEventResponse {
        public String user;

        public String liquidator;

        public BigInteger debtToCover;
    }

    public static class OwnershipTransferredEventResponse extends BaseEventResponse {
        public String previousOwner;

        public String newOwner;
    }

    public static class PausedEventResponse extends BaseEventResponse {
        public String account;
    }

    public static class TokenBurnEventEventResponse extends BaseEventResponse {
        public String from;

        public BigInteger amount;
    }

    public static class TokenMintEventEventResponse extends BaseEventResponse {
        public String to;

        public BigInteger amount;
    }

    public static class TokenTransferEventEventResponse extends BaseEventResponse {
        public String from;

        public String to;

        public BigInteger amount;
    }

    public static class UnpausedEventResponse extends BaseEventResponse {
        public String account;
    }
}
