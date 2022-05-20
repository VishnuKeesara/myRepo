/**
 * Module: OhiResponseDocumentMapper.java	   
 * 
 * @author jrobson
 * @created Aug 12, 2011
 *
 * All contents of this file is confidential property of Harvard Pilgrim
 * Health Corporation.
 *
 * No part of this file may be reproduced, modified or transmitted in
 * whole or part or in any form or by any means, electronic or mechanical,
 * for any purpose, without the express written permission of Harvard
 * Pilgrim Health Corporation.
 *
 * CopyRight(c) 2009 by Harvard Pilgrim Health Organization.
 * All rights reserved.
 */
package org.hphc.services.claim.service.util.mapper;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.hphc.constants.Constants;
import org.hphc.decode.vo.CodeSet;
import org.hphc.schema.claim.v6.AmountType;
import org.hphc.schema.claim.v6.Diagnoses;
////import org.hphc.schema.claim.v6.DiagnosesDocument.Diagnoses;
import org.hphc.schema.claim.v6.DiagnosisType;
import org.hphc.schema.claim.v6.ExternalEditorType;
import org.hphc.schema.claim.v6.HippaGroupCodes;
import org.hphc.schema.claim.v6.OrigPickMember;
import org.hphc.schema.claim.v6.HippaGroupCodes.HippaGroupCode;
////import org.hphc.schema.claim.v6.HippaGroupCodesDocument.HippaGroupCodes;
////import org.hphc.schema.claim.v6.HippaGroupCodesDocument.HippaGroupCodes.HippaGroupCode;
import org.hphc.schema.claim.v6.InpatientDayCountEnumType;
import org.hphc.schema.claim.v6.InpatientDayCountType;
import org.hphc.schema.claim.v6.InternalMessages;
import org.hphc.schema.claim.v6.InternalMessages.InternalMessage;
////import org.hphc.schema.claim.v6.InternalMessagesDocument.InternalMessages;
////import org.hphc.schema.claim.v6.InternalMessagesDocument.InternalMessages.InternalMessage;
import org.hphc.schema.claim.v6.MedicalBehClaimType;
import org.hphc.schema.claim.v6.MedicalBehClaimType.ClaimNumbers;
import org.hphc.schema.claim.v6.MedicalBehClaimType.ConditionCodes;
import org.hphc.schema.claim.v6.MedicalBehClaimType.DiagnosisRelatedGrps;
import org.hphc.schema.claim.v6.MedicalBehClaimType.DiagnosisRelatedGrps.DiagnosisRelatedGrp;
import org.hphc.schema.claim.v6.MedicalBehClaimType.Services;
import org.hphc.schema.claim.v6.MedicalBehClaimType.UnfinalizedDocumentNumbers;
import org.hphc.schema.claim.v6.MedicalBehClaimType.UnfinalizedDocumentNumbers.UnfinalizedDocumentNumber;
import org.hphc.schema.claim.v6.MedicalBehClaimType.UnfinalizedReasons;
import org.hphc.schema.claim.v6.MedicalBehServiceLineType;
import org.hphc.schema.claim.v6.MedicalBehServiceLineType.AdjudicationBenefitCodes;
import org.hphc.schema.claim.v6.MedicalBehServiceLineType.Amounts;
import org.hphc.schema.claim.v6.MedicalBehServiceLineType.AnesthesiaMinutes;
import org.hphc.schema.claim.v6.MedicalBehServiceLineType.AuthorizationNbrs;
import org.hphc.schema.claim.v6.MedicalBehServiceLineType.Counts;
import org.hphc.schema.claim.v6.MedicalBehServiceLineType.Counts.Count;
import org.hphc.schema.claim.v6.MedicalBehServiceLineType.ExternalEditrReasonCodesText;
import org.hphc.schema.claim.v6.MedicalBehServiceLineType.InpatientDayCounts;
////import org.hphc.schema.claim.v6.MedicalBehServiceLineType.Minutes;
////import org.hphc.schema.claim.v6.MedicalBehServiceLineType.Minutes.Minutes2;
import org.hphc.schema.claim.v6.MedicalBehServiceLineType.Modifiers;
import org.hphc.schema.claim.v6.MedicalBehServiceLineType.Modifiers.Modifier;
import org.hphc.schema.claim.v6.MedicalBehServiceLineType.PaymentDetails;
import org.hphc.schema.claim.v6.MedicalBehServiceLineType.PaymentDetails.PaymentDetail;
import org.hphc.schema.claim.v6.Member;
import org.hphc.schema.claim.v6.MemberClaimType.MedicalClaims;
////import org.hphc.schema.claim.v6.MemberType;
import org.hphc.schema.claim.v6.PayToType;
import org.hphc.schema.claim.v6.Procedures;
import org.hphc.schema.claim.v6.Procedures.Procedure;
import org.hphc.schema.claim.v6.ProviderIdTypeEnumType;
import org.hphc.schema.claim.v6.ProviderType;
import org.hphc.schema.claim.v6.ProviderType.Ids;
import org.hphc.schema.claim.v6.ProvidersType;
import org.hphc.schema.claim.v6.RemarkLineType;
import org.hphc.schema.claim.v6.RemarkType;
import org.hphc.schema.claim.v6.Remarks;
////import org.hphc.schema.claim.v6.RemarksType;
import org.hphc.schema.claim.v6.UbBillType;
import org.hphc.schema.datatypes.v2.DecodeValueType;
import org.hphc.schema.datatypes.v2.DecodeValueType.Code;
import org.hphc.schema.datatypes.v2.IdType;
import org.hphc.schema.datatypes.v2.LocationType;
import org.hphc.schema.datatypes.v2.LocationType.AddressLines;
import org.hphc.schema.datatypes.v2.LocationType.AddressLines.AddressLine;
import org.hphc.schema.datatypes.v2.ObjectFactory;
import org.hphc.schema.datatypes.v2.PersonNameType;
import org.hphc.services.claim.dao.entities.ApNoChkPayeeClaimInvoice;
import org.hphc.services.claim.dao.entities.DfltPayeeClaimInvoice;
import org.hphc.services.claim.dao.entities.HphcFileClaimInvoiceV;
import org.hphc.services.claim.dao.entities.HphcFileClaimPaymentV;
import org.hphc.services.claim.dao.entities.StgCl;
import org.hphc.services.claim.dao.entities.StgClCond;
import org.hphc.services.claim.dao.entities.StgClSvc;
import org.hphc.services.claim.dao.entities.StgClSvcAudit;
import org.hphc.services.claim.dao.entities.StgClSvcMsgPendXrefVw;
import org.hphc.services.claim.dao.entities.StgClTpPrcssElgbl;
import org.hphc.services.claim.dao.interfaces.ConditionCodeDao;
import org.hphc.services.claim.dao.interfaces.IHphcFileClaimInvoiceDao;
import org.hphc.services.claim.dao.interfaces.IInternalMessagesDao;
import org.hphc.services.claim.dao.interfaces.StgClTpPrcssElgblDao;
import org.hphc.services.claim.service.util.ClaimConstants;
import org.hphc.services.claim.service.util.ClaimDecodeUtil;
import org.hphc.services.claim.service.util.ClaimResultCodeHandler;
import org.hphc.services.claim.service.util.CodeDecodeUtil;
import org.hphc.services.claim.service.util.CodeDecodeUtilContext;
import org.hphc.services.claim.service.util.OhiMedicalServiceLineEffectiveDateComparator;
import org.hphc.services.claim.service.util.OhiMedicalServiceLineEndDateComparator;
import org.hphc.services.claim.service.util.OhiMedicalServiceLinePaidDateComparator;
import org.hphc.services.claim.service.util.ResponseCriteriaVo;
import org.hphc.services.util.LoggingThreadContext;
import org.hphc.services.util.QueryBudgetContext;
import org.hphc.services.util.XMLUtil;


/**
 * 
 *
 */
public class OhiClaimsResponseDocMapper extends ResponseDocumentUtil {

	private static final Logger LOGGER = Logger.getLogger(OhiClaimsResponseDocMapper.class.toString());
	private IInternalMessagesDao internalMsgDao;
	private IHphcFileClaimInvoiceDao invoiceDao;
	private ClaimDecodeUtil claimDecodeUtil;
	private CodeDecodeUtilContext codeDecodeUtilContext;
	private ClaimResultCodeHandler claimResultCodeHandler;
	private ConditionCodeDao conditionCodeDao;
	private StgClTpPrcssElgblDao stgClTpPrcssElgblDao;

	/**
	 * @param claimResultCodeHandler the claimResultCodeHandler to set
	 */
	public void setClaimResultCodeHandler(
			ClaimResultCodeHandler claimResultCodeHandler) {
		this.claimResultCodeHandler = claimResultCodeHandler;
	}
	
	/**
	 * @param codeDecodeUtilContext the codeDecodeUtilContext to set
	 */
	public void setCodeDecodeUtilContext(CodeDecodeUtilContext codeDecodeUtilContext) {
		this.codeDecodeUtilContext = codeDecodeUtilContext;
	}

	/**
	 * @param claimDecodeUtil the claimDecodeUtil to set
	 */
	public void setClaimDecodeUtil(ClaimDecodeUtil claimDecodeUtil) {
		this.claimDecodeUtil = claimDecodeUtil;
	}

	/**
	 * @param internalMsgDao the internalMsgDao to set
	 */
	public void setInternalMsgDao(IInternalMessagesDao internalMsgDao) {
		this.internalMsgDao = internalMsgDao;
	}


	/**
	 * @param invoiceDao the invoiceDao to set
	 */
	public void setInvoiceDao(IHphcFileClaimInvoiceDao invoiceDao) {
		this.invoiceDao = invoiceDao;
	}
	
	/**
	 * @param conditionCodeDao the conditionCodeDao to set
	 */
	public void setConditionCodeDao(ConditionCodeDao conditionCodeDao) {
		this.conditionCodeDao = conditionCodeDao;
	}

	public StgClTpPrcssElgblDao getStgClTpPrcssElgblDao() {
		return stgClTpPrcssElgblDao;
	}

	public void setStgClTpPrcssElgblDao(StgClTpPrcssElgblDao stgClTpPrcssElgblDao) {
		this.stgClTpPrcssElgblDao = stgClTpPrcssElgblDao;
	}

	/**
	 * @param stgCls
	 * @param medicalClaims
	 * @param responseCriteria
	 */
	public void addMedicalClaim(List<StgCl> stgCls,
			MedicalClaims medicalClaims,ResponseCriteriaVo responseCriteria) {
		
		Map<Long, List<StgClSvcMsgPendXrefVw>> serviceInternalMsgsMap = null;
		Map<String, Set<HphcFileClaimInvoiceV>> claimInvoiceMap = null;
		Map<String, Set<DfltPayeeClaimInvoice>> deniedClaimInvoiceMap = null;
		CodeDecodeUtil codeDecodeUtil = new CodeDecodeUtil(codeDecodeUtilContext);
		Collections.sort(stgCls);	
		Map<Long, StgClSvcAudit> serviceAuditMap = null;
		Map<Long,StgClCond>conditionCodeMap=null;
		Map<String,StgClTpPrcssElgbl>externalEditorMap=null;
		Map<String, Set<ApNoChkPayeeClaimInvoice>> apNoChkClaimInvoiceMap = null;
		
		//Get Claim Messages - Pend Reasons and Internal Messages
		if(!responseCriteria.getResultContentSummary()){
			serviceInternalMsgsMap = getClaimMessages(stgCls);			
		}
		
		//Get Service lines Audits info - R2:000021670647
		if(!responseCriteria.getResultContentSummary()){
			serviceAuditMap = getServiceAudit(stgCls);					
		}
		
		//Get Condition code
		if(!responseCriteria.getResultContentSummary()){
			conditionCodeMap = getConditionCode(stgCls);					
		 }
		//Get External Editors
		//if(!responseCriteria.getResultContentSummary()){
			externalEditorMap = getExternalEditor(stgCls);					
		// }
		
		
		//Decode all claim codes if the decode flag is true
		codeDecodeUtil.decodeOHICodes(stgCls, serviceInternalMsgsMap, responseCriteria.getCodeDecodeFlag(),conditionCodeMap,externalEditorMap);
		
		/*Decode Unfinalized Reason Codes
		 *User Story US31206: Removing unfinalized reason codes from getclaim response in V6 
			//codeDecodeUtil.decodeUnfinalizedReasonCodes(stgCls, responseCriteria.getCodeDecodeFlag());
		 */
		//Fetch Check Details
		if(responseCriteria.getPaymentDetailFlag()){
			claimInvoiceMap = getCheckDetails(stgCls);
			deniedClaimInvoiceMap = getDeniedClaimCheckDetails(stgCls);
			apNoChkClaimInvoiceMap = getApNoChkClaimDetails(stgCls);
		}

		for (StgCl claim : stgCls) {
			MedicalBehClaimType  mBEHClaim =  new MedicalBehClaimType();
			medicalClaims.getMedicalClaims().add(mBEHClaim);

			//Set Claim Source
			mBEHClaim.setClaimSource(MedicalBehClaimType.ClaimSource.OHI); 

			//Set Claim Number. Append multisystem indicator and claim suffix
			if(hasValue(claim.getOhiClNbr())){
				ClaimNumbers claimIds = new ClaimNumbers();
				mBEHClaim.setClaimNumbers(claimIds);
				IdType claimId = new IdType();
				claimIds.getClaimNumbers().add(claimId);
				StringBuilder claimNumber = new StringBuilder(claim.getOhiClNbr().trim());
				//Set multisystem Indicator
				if(hasValue(claim.getMultSysCd())
						&& ClaimConstants.MULTISYSTEM_INDICATOR.equalsIgnoreCase(claim.getMultSysCd().trim())){ 
					
						mBEHClaim.setMultiSystemIndicator(claim.getMultSysCd().trim());
						claimNumber.append(claim.getMultSysCd().trim());
				}
				//Set term Indicator
				if(claim.getClTermInd() != null){
					mBEHClaim.setTerminatedIndicator(claim.getClTermInd().toString());
					if(hasValue(claim.getOcsaClSufNbr())
							&& ClaimConstants.TERM_INDICATOR.equalsIgnoreCase(claim.getClTermInd().toString())){
						claimNumber.append(claim.getOcsaClSufNbr().trim());
					}
				}
				claimId.setValue(claimNumber.toString());
				claimId.setIdSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
				claimId.setType(ClaimConstants.HPHC);
				
				//A third party number that represents a Claim.
				if(hasValue(claim.getTpClNbr())){ 
					StringBuilder tpclaimNumber = new StringBuilder(claim.getTpClNbr().trim());
					IdType thirdPartyClaimId = new IdType();
					claimIds.getClaimNumbers().add(thirdPartyClaimId);
					thirdPartyClaimId.setValue(tpclaimNumber.toString());
					thirdPartyClaimId.setIdSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
					thirdPartyClaimId.setType(ClaimConstants.VENDOR);
				}
			}
			//Set Claim channel (Paper or electronic claim)
			if(hasValue(claim.getClFmtCd())){
				mBEHClaim.setClaimChannel(codeDecodeUtil.decodeChannelCode(claim.getClFmtCd().trim()));
				
				MedicalBehClaimType.ClaimType claimType = new MedicalBehClaimType.ClaimType();
				mBEHClaim.setClaimType(claimType);
				claimType.setCode(claim.getClFmtCd().trim());
				claimType.setValue(codeDecodeUtil.decodeClaimTypeCode(claim.getClFmtCd().trim()));
				claimType.setCodeSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
			}
			
			//set multiProviderIndicator			
			if(claim.getMultPvInd() != null){
				mBEHClaim.setMultiProviderIndicator(claim.getMultPvInd().toString());
			}
			
			//Set ClassificationCode 
			if(hasValue(claim.getClassCd())){
				MedicalBehClaimType.ClassificationCode classificationCode = new MedicalBehClaimType.ClassificationCode();
				mBEHClaim.setClassificationCode(classificationCode);
				classificationCode.setCode(claim.getClassCd().trim());
				classificationCode.setValue(claim.getClassCd().trim());				
				classificationCode.setCodeSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
			}
			
			//Set Classification Schema Code 
			if(hasValue(claim.getClassSchemeCd())){
				MedicalBehClaimType.ClassificationSchemeCode classSchemaCode = new MedicalBehClaimType.ClassificationSchemeCode();
				mBEHClaim.setClassificationSchemeCode(classSchemaCode);				
				classSchemaCode.setCode(claim.getClassSchemeCd().trim());				
				classSchemaCode.setValue(codeDecodeUtil.decodeClassificationSchemaCodes(claim.getClassSchemeCd().trim()));
				classSchemaCode.setCodeSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());				
			}
			
			// add External Editors			
			addExternalEditors(claim, mBEHClaim,codeDecodeUtil,externalEditorMap);
			
			
			// added for cotiviti
			/*if((claim.getExtnalEditrAppliedClaimVersionNbr() != null)){
				////ValueType ExtnalEditrAppliedClaimVersionNbr = mBEHClaim.addNewExtnalEditrAppliedClaimVersionNbr();
				MedicalBehClaimType.ExtnalEditrAppliedClaimVersionNbr ExtnalEditrAppliedClaimVersionNbr = new MedicalBehClaimType.ExtnalEditrAppliedClaimVersionNbr();
				mBEHClaim.setExtnalEditrAppliedClaimVersionNbr(ExtnalEditrAppliedClaimVersionNbr);		
				ExtnalEditrAppliedClaimVersionNbr.setValue(claim.getExtnalEditrAppliedClaimVersionNbr().toString().trim());
			}*/
			// added for cotiviti
			/*if(claim.getExternalEditrEligibilityIndi() != null){				
				mBEHClaim.setExternalEditrEligibilityIndi(claim.getExternalEditrEligibilityIndi().toString());											
			}
			// added for cotiviti
			if(claim.getExternalEditorStatusCode() != null){
				mBEHClaim.setExternalEditorStatusCode(claim.getExternalEditorStatusCode().toString());											
			}	*/		
			//Set entrySourceType
			if(hasValue(claim.getEntrySrcTypCd())){
				MedicalBehClaimType.EntrySourceType entrySourceType = new MedicalBehClaimType.EntrySourceType();
				mBEHClaim.setEntrySourceType(entrySourceType);				
				entrySourceType.setCode(claim.getEntrySrcTypCd().trim());
				entrySourceType.setValue(codeDecodeUtil.decodeEntrySourceType(claim.getEntrySrcTypCd().trim()));
				entrySourceType.setCodeSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());				
			}

			// Medical claims conditionCode
			addClaimConditionCode(claim,conditionCodeMap, mBEHClaim,codeDecodeUtil);
			
			//diagnosisRelGrpCd for Medical claims diagnosisRelGrpCd
			addClaimDiagnosisRelGrpCd(claim, mBEHClaim,codeDecodeUtil);
			//Add Member Information
			if(hasValue(claim.getMbNbr())){				
				Member member = new Member();
				mBEHClaim.setMember(member);
				//Add member id				
				IdType memberId =  new IdType();
				member.setId(memberId);
				memberId.setValue(claim.getMbNbr().trim());
				
				// Add Joint Ventur Indicator
				if(claim.getJvHphcMbInd() != null)
				{
				   member.setJointVentureIndicator(claim.getJvHphcMbInd().toString()) ;
				}

			}
			
			//Add NH newborn OrigPickMember Information
			if(hasValue(claim.getOrigPickMbNbr())){
				OrigPickMember origPickMember = new OrigPickMember();
				mBEHClaim.setOrigPickMember(origPickMember);
				
				IdType origPickMemberId =  new IdType();
				origPickMember.setId(origPickMemberId);
				origPickMemberId.setValue(claim.getOrigPickMbNbr().trim());
			}	
			
			//Add Servicing Provider
			//setting first provider number from first service line
			if(hasValue(claim.getSvcPvNbr())){
				ProvidersType providers = new ProvidersType();
				mBEHClaim.setProviders(providers);
				ProviderType provider = new ProviderType();
				providers.getProviders().add(provider);
				provider.setRole(org.hphc.schema.claim.v6.ProviderRoleEnumType.SERVICING);				
				Ids providerIds = new Ids();
				provider.setIds(providerIds);				
				
				ProviderType.Ids.Id providerId = new ProviderType.Ids.Id();
				providerIds.getIds().add(providerId);
				providerId.setValue(claim.getSvcPvNbr().trim());
				providerId.setIdSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
				providerId.setRole(org.hphc.schema.claim.v6.ProviderRoleEnumType.SERVICING);
				providerId.setIdType(ProviderIdTypeEnumType.PROVIDER);
				
				//Get Affliation Id from service line
				String affiliationId = getClaimProviderAffiliationId(claim);
				if(hasValue(affiliationId)){					
					ProviderType.Ids.Id pId = new ProviderType.Ids.Id();
					providerIds.getIds().add(pId);					
					pId.setValue(affiliationId.trim());
					pId.setIdSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
					pId.setRole(org.hphc.schema.claim.v6.ProviderRoleEnumType.SERVICING);
					pId.setIdType(ProviderIdTypeEnumType.AFFILIATION);
				}

				
				//Get NPI from Service line and set at header
				String npi = getClaimProviderNPI(claim);
				if(hasValue(npi)){
					ProviderType.Ids.Id pId = new ProviderType.Ids.Id();
					providerIds.getIds().add(pId);
					pId.setValue(npi.trim());
					pId.setIdSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
					pId.setRole(org.hphc.schema.claim.v6.ProviderRoleEnumType.SERVICING);
					pId.setIdType(ProviderIdTypeEnumType.NPI);
				}
								
				//Add Servicing Provider Name
				if(hasValue(claim.getProviderName())){
					PersonNameType nameType = new PersonNameType();
					provider.setName(nameType);
					nameType.setFormattedName(claim.getProviderName().trim());
				}
				//add Drived code @claim
				if(hasValue(claim.getClOrigDerivSvcPvCd())){
					ProviderType.DerivedCode drivedCode= new ProviderType.DerivedCode();
					provider.setDerivedCode(drivedCode);
					drivedCode.setCode(claim.getClOrigDerivSvcPvCd().trim());					
					drivedCode.setValue(codeDecodeUtil.decodeProviderDrivedCodes(claim.getClOrigDerivSvcPvCd().trim()));	
				}
			}
//			
//		kmck	
//			if( hasValue(claim.getPrcApplCd()) )
//			{
//				ValueType pricerAppCode = mBEHClaim.addNewPricerApplicationCode();
//				pricerAppCode.setCode(claim.getPrcApplCd().trim());
//				pricerAppCode.setStringValue(claim.getPrcApplCd().trim());
//				pricerAppCode.setCodeSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
//
//			}
			
			
			
			// The identification code of the third party servicing Provider.
			//6/26/2012: Currently for UMR Claims, this is the SVC PV MPIN, TIN, and TIN Suffix.
			if(hasValue(claim.getTpSvcPvAltCd()))
			{
				ProvidersType thirdPartyProviders = new ProvidersType();
				mBEHClaim.setThirdPartyProviders(thirdPartyProviders);				
				ProviderType thridPartyProvider = new ProviderType();
				thirdPartyProviders.getProviders().add(thridPartyProvider);
				
				thridPartyProvider.setRole(org.hphc.schema.claim.v6.ProviderRoleEnumType.SERVICING);

				Ids tpproviderIds = new Ids();
				thridPartyProvider.setIds(tpproviderIds);
				
				ProviderType.Ids.Id providerId = new ProviderType.Ids.Id();
				tpproviderIds.getIds().add(providerId);
				providerId.setValue(claim.getTpSvcPvAltCd().trim());
				providerId.setIdSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
				providerId.setRole(org.hphc.schema.claim.v6.ProviderRoleEnumType.SERVICING);
				providerId.setIdType(ProviderIdTypeEnumType.PROVIDER);
				
			}

			//add aggregated amounts on claim level
			addMedicalAggregateAmounts(claim, mBEHClaim, responseCriteria.getResultContentSummary());

			// A code that indicates which enity priced the claim.  
			//EXAMPLES: 1) UMC = UnitedHealthcare  2) AMC = AmisysC
			//3) OHP = OHI Pricer
			if( hasValue(claim.getPrcApplCd()) )
			{
				MedicalBehClaimType.PricerApplicationCode pricerAppCode = new MedicalBehClaimType.PricerApplicationCode();
				mBEHClaim.setPricerApplicationCode(pricerAppCode);
				pricerAppCode.setCode(claim.getPrcApplCd().trim());
				pricerAppCode.setValue(claim.getPrcApplCd().trim());
				pricerAppCode.setCodeSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());

			}
			
			if( hasValue(claim.getDischStatusCd()) )
			{
				MedicalBehClaimType.DischargeStatusCode dischargeStatusCode = new MedicalBehClaimType.DischargeStatusCode();
				mBEHClaim.setDischargeStatusCode(dischargeStatusCode);
						
				dischargeStatusCode.setCode(claim.getDischStatusCd().trim());
				dischargeStatusCode.setValue(claim.getDischStatusCd().trim());
				dischargeStatusCode.setCodeSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
			}
			
			if( hasValue(claim.getSubPrcCd()) )
			{
				MedicalBehClaimType.SubPricerCode subPricerCode = new MedicalBehClaimType.SubPricerCode();
				mBEHClaim.setSubPricerCode(subPricerCode);		
				subPricerCode.setCode(claim.getSubPrcCd().trim());
				subPricerCode.setValue(claim.getSubPrcCd().trim());
				subPricerCode.setCodeSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
			}
			
			// A proprietary claim level iCES code returned to Pricer.
			if( hasValue(claim.getPrcClTpSftwStatCd()) )
			{
				MedicalBehClaimType.PricerClaimICESResultCode pricerResultCode = new MedicalBehClaimType.PricerClaimICESResultCode();
				mBEHClaim.setPricerClaimICESResultCode(pricerResultCode);	
				pricerResultCode.setCode(claim.getPrcClTpSftwStatCd().trim());
				pricerResultCode.setValue(claim.getPrcClTpSftwStatCd().trim());
				pricerResultCode.setCodeSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
			}
			
			if( claim.getHippaCrctClInd() != null )
			{
				mBEHClaim.setCorrectedClaimIndicator(claim.getHippaCrctClInd().toString()) ;
			}
		
			//OHI Remarks
			if(claim.getOhiEextnlRemrkTxt() != null){
				mBEHClaim.setExternalRemarks(claim.getOhiEextnlRemrkTxt()) ;
			}
			
			// A code that represents the third party network that priced the claim as part of the 
			// HPHC joint venture product.  
			// EXAMPLES:  1) UHC   Options PPO  2) MUL   
			//Multiplan  3)  BEE   Beechstreet Supplemental Network
			if( hasValue(claim.getTpNtwrkCd()) )
			{
				MedicalBehClaimType.ThirdPartyNetworkCode  thirdPartyNetworkCode = new MedicalBehClaimType.ThirdPartyNetworkCode ();
				mBEHClaim.setThirdPartyNetworkCode(thirdPartyNetworkCode);					
				thirdPartyNetworkCode.setCode(claim.getTpNtwrkCd().trim());
				thirdPartyNetworkCode.setValue(claim.getTpNtwrkCd().trim());
				thirdPartyNetworkCode.setCodeSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
			}
			//  A code that represents the network type that priced the claim. 
						// EXAMPLES:  1) PAR = Participating  2) SHR = Shared  3) NON = Non Participating 4) UNK = Unknown
						// This code is only populated for UMR Priced claims.
			if( hasValue(claim.getTpNtwrkTypCd()) )
			{
				MedicalBehClaimType.ThirdPartyNetworkTypeCode  thirdPartyNetworkTypeCode = new MedicalBehClaimType.ThirdPartyNetworkTypeCode ();
				mBEHClaim.setThirdPartyNetworkTypeCode(thirdPartyNetworkTypeCode);					
				thirdPartyNetworkTypeCode.setCode(claim.getTpNtwrkTypCd().trim());
				thirdPartyNetworkTypeCode.setValue(claim.getTpNtwrkTypCd().trim());
				thirdPartyNetworkTypeCode.setCodeSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
			}
// JointVenture
			//A HIPAA standard code that represents the financially responsible party.  
			//EXAMPLES: CO:Contractual Obligation
			//CR: Correction and Reversal
			//OA: Other Adjustment
			//PR: Patient Responsibility
//			if(hasValue(claim.getClHippaGrp1Cd()) || hasValue(claim.getClHippaGrp2Cd()) || hasValue(claim.getClHippaGrp3Cd()) ||
//			   hasValue(claim.getClHippaGrp4Cd()) || hasValue(claim.getClHippaGrp5Cd())	)
//			{
//				HippaGroupCodes hippaGroupCodes = mBEHClaim.addNewHippaGroupCodes() ;	
//
//				if( hasValue(claim.getClHippaGrp1Cd()) )
//				{
//					HippaGroupCode theHippaGroupCode = hippaGroupCodes.addNewHippaGroupCode() ;
//					
//					theHippaGroupCode.setCode(claim.getClHippaGrp1Cd().trim());
//					theHippaGroupCode.setStringValue(claim.getClHippaGrp1Cd().trim());
//					theHippaGroupCode.setCodeSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
//					theHippaGroupCode.setIndex(new BigInteger("1"));
//				}
//				
//				if( hasValue(claim.getClHippaGrp2Cd()) )
//				{
//					HippaGroupCode theHippaGroupCode = hippaGroupCodes.addNewHippaGroupCode() ;
//					
//					theHippaGroupCode.setCode(claim.getClHippaGrp2Cd().trim());
//					theHippaGroupCode.setStringValue(claim.getClHippaGrp2Cd().trim());
//					theHippaGroupCode.setCodeSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
//					theHippaGroupCode.setIndex(new BigInteger("2"));
//				}
//				
//				if( hasValue(claim.getClHippaGrp3Cd()) )
//				{
//					HippaGroupCode theHippaGroupCode = hippaGroupCodes.addNewHippaGroupCode() ;
//					
//					theHippaGroupCode.setCode(claim.getClHippaGrp3Cd().trim());
//					theHippaGroupCode.setStringValue(claim.getClHippaGrp3Cd().trim());
//					theHippaGroupCode.setCodeSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
//					theHippaGroupCode.setIndex(new BigInteger("3"));
//				}
//				
//				if( hasValue(claim.getClHippaGrp4Cd()) )
//				{
//					HippaGroupCode theHippaGroupCode = hippaGroupCodes.addNewHippaGroupCode() ;
//					
//					theHippaGroupCode.setCode(claim.getClHippaGrp4Cd().trim());
//					theHippaGroupCode.setStringValue(claim.getClHippaGrp4Cd().trim());
//					theHippaGroupCode.setCodeSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
//					theHippaGroupCode.setIndex(new BigInteger("4"));
//				}
//				
//				if( hasValue(claim.getClHippaGrp5Cd()) )
//				{
//					HippaGroupCode theHippaGroupCode = hippaGroupCodes.addNewHippaGroupCode() ;
//					
//					theHippaGroupCode.setCode(claim.getClHippaGrp5Cd().trim());
//					theHippaGroupCode.setStringValue(claim.getClHippaGrp5Cd().trim());
//					theHippaGroupCode.setCodeSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
//					theHippaGroupCode.setIndex(new BigInteger("5"));
//				}
//				
//			}
			
			
			//Provider patient account number
			if(hasValue(claim.getPatCntlNbr())){
				mBEHClaim.setProviderPatientAccountNbr(claim.getPatCntlNbr().trim());
			}

			//Medical Record number
			if(hasValue(claim.getMedrecNbr())){
				mBEHClaim.setMedicalRecordNbr(claim.getMedrecNbr().trim());
			}

			//Service Begin Date
			if(responseCriteria.getResultContentSummary()){
				Date serviceStartDate = getClaimServiceBeginDate(claim);
				if(serviceStartDate != null){
					mBEHClaim.setServiceBeginDate(XMLUtil.getXMLDate(serviceStartDate));
				}
			}else{
				if(claim.getSubmStmtFromDt() != null){
					mBEHClaim.setServiceBeginDate(XMLUtil.getXMLDate(claim.getSubmStmtFromDt()));
				}
			}
			
			//Service End Date
			if(responseCriteria.getResultContentSummary()){
				Date serviceEndDate = getClaimServiceEndDate(claim);
				if(serviceEndDate != null){
					mBEHClaim.setServiceEndDate(XMLUtil.getXMLDate(serviceEndDate));
				}
			}else{
				if(claim.getSubmStmtToDt() != null){
					mBEHClaim.setServiceEndDate(XMLUtil.getXMLDate(claim.getSubmStmtToDt()));
				}
			}

			//Final Adjudication Date
			Date finalAdjudicationDate = getClaimAdjudicationDate(claim);
			if(finalAdjudicationDate != null){
				mBEHClaim.setFinalAdjudicationDate(XMLUtil.getXMLDate(finalAdjudicationDate));
			}

			if(claim.getHphcRcvDt() != null){
				mBEHClaim.setReceiptDate(XMLUtil.getXMLDate(claim.getHphcRcvDt()));
			}
			
			if(claim.getOhiEventDt() != null){
				mBEHClaim.setTransactionDate(XMLUtil.getXMLDate(claim.getOhiEventDt()));
			} 
			
			// If the claim is finalized 
			if(claim.getLatestFinalizedDate() != null )
			{
				mBEHClaim.setLatestFinalizedDate(XMLUtil.getXMLDate(claim.getLatestFinalizedDate()));				
			}
			
			//The date on which a third party received the Claim.
			if(claim.getTpClRcvDt() != null){
				mBEHClaim.setThirdPartyReceiptDate(XMLUtil.getXMLDate(claim.getTpClRcvDt()));					
			} 
			
			
			if(claim.getOcsaUpdtDt() != null){
				mBEHClaim.setUpdateDate(XMLUtil.getXMLDate(claim.getOcsaUpdtDt()));					
			}
			
			//ICD Indicator
			if(claim.getClIcdRevisNbr() != null){
				mBEHClaim.setIcdRevisionNumber(claim.getClIcdRevisNbr().toString());
			}

			//diagnosis code for Medical claims
			addClaimDiagnosis(claim, mBEHClaim,codeDecodeUtil);
						
			//procedure code for Medical claims
			addClaimProcedureCodes(claim, mBEHClaim,codeDecodeUtil);

			// kmck
			if(hasValue(claim.getPrcSvcPvTinNbr())){
				mBEHClaim.setFederalTaxNumber(claim.getPrcSvcPvTinNbr()) ;
			}
			
			if(hasValue(claim.getPrcDrgCd())){
				mBEHClaim.setDiagnosisRelGrpCd(claim.getPrcDrgCd().trim());
			}

			if(responseCriteria.getHipaaCodeFlag()){
				// Add Hipaa Status Codes
				addClaimStatusCodes(claim, mBEHClaim);

				// Add Hipaa Category Status Codes
				addClaimCategoryStatusCodes(claim, mBEHClaim);
			}

			// Add UB Bill Type
			addUBBillType(claim, mBEHClaim,codeDecodeUtil);

			//add life cycle state
			if(hasValue(claim.getAdjudClDispCd())){
				MedicalBehClaimType.LifeCycleState  claimStatus = new MedicalBehClaimType.LifeCycleState();
				mBEHClaim.setLifeCycleState(claimStatus);					
				claimStatus.setCode(claim.getAdjudClDispCd().trim());
				claimStatus.setValue(claim.getAdjudClDispCd());
				claimStatus.setCodeSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
			}
			
			// Add Service Category in summary response
			if(responseCriteria.getResultContentSummary()){
				addServiceCategories(claim,mBEHClaim,codeDecodeUtil);
			}

			if(!responseCriteria.getResultContentSummary()){
				addMedicalClaimServiceLines(claim, mBEHClaim, responseCriteria, serviceInternalMsgsMap, claimInvoiceMap, deniedClaimInvoiceMap, codeDecodeUtil,serviceAuditMap, apNoChkClaimInvoiceMap);
			}
			
			//Add unfinalized reason codes
			addUnfinalizedreasonCodes(claim, mBEHClaim,codeDecodeUtil);
			
			if(hasValue(claim.getOhiClUnfnlzDoc1Nbr()) 
					||hasValue(claim.getOhiClUnfnlzDoc2Nbr()) 
					|| hasValue(claim.getOhiClUnfnlzDoc3Nbr())){
				
				UnfinalizedDocumentNumbers unfinalizedDocNo = new UnfinalizedDocumentNumbers();
				mBEHClaim.setUnfinalizedDocumentNumbers(unfinalizedDocNo);
				if(hasValue(claim.getOhiClUnfnlzDoc1Nbr())){
					UnfinalizedDocumentNumber docNumber = new UnfinalizedDocumentNumber();
					unfinalizedDocNo.getUnfinalizedDocumentNumbers().add(docNumber);
					docNumber.setIndex(new BigInteger("1").toString());
					docNumber.setValue(claim.getOhiClUnfnlzDoc1Nbr().trim());
				}
				if(hasValue(claim.getOhiClUnfnlzDoc2Nbr())){
					UnfinalizedDocumentNumber docNumber = new UnfinalizedDocumentNumber();
					unfinalizedDocNo.getUnfinalizedDocumentNumbers().add(docNumber);
					docNumber.setIndex(new BigInteger("2").toString());
					docNumber.setValue(claim.getOhiClUnfnlzDoc2Nbr().trim());					
				}
				if(hasValue(claim.getOhiClUnfnlzDoc3Nbr())){
					UnfinalizedDocumentNumber docNumber = new UnfinalizedDocumentNumber();
					unfinalizedDocNo.getUnfinalizedDocumentNumbers().add(docNumber);
					docNumber.setIndex(new BigInteger("3").toString());
					docNumber.setValue(claim.getOhiClUnfnlzDoc3Nbr().trim());					
				}
			}
			 if(responseCriteria.getRemarksFlag()){
				 addClaimRemarks(claim, mBEHClaim);
			 }
			 
		}
	}

	/**
	 * Add service lines
	 * 
	 * @param claim
	 * @param mBEHClaim
	 * @param responseCriteria
	 * @param serviceInternalMsgsMap
	 * @param claimInvoiceMap
	 * @param deniedClaimInvoiceMap
	 * @param codeDecodeUtil
	 */
	private void addMedicalClaimServiceLines(StgCl claim,
			MedicalBehClaimType mBEHClaim, ResponseCriteriaVo responseCriteria,
			Map<Long, List<StgClSvcMsgPendXrefVw>> serviceInternalMsgsMap,
			Map<String, Set<HphcFileClaimInvoiceV>> claimInvoiceMap,
			Map<String, Set<DfltPayeeClaimInvoice>> deniedClaimInvoiceMap,
			CodeDecodeUtil codeDecodeUtil ,Map<Long,StgClSvcAudit> serviceAuditMap, Map<String, Set<ApNoChkPayeeClaimInvoice>> apNoChkClaimInvoiceMap) {

		Services services = new Services();
		mBEHClaim.setServices(services);
		Set<StgClSvc> serviceLines = claim.getServices();
		QueryBudgetContext qbc = LoggingThreadContext.getQueryBudgetContext();
		qbc.setOhiMedicalServiceTotal(qbc.getOhiMedicalServiceTotal()+serviceLines.size());
		TreeSet<StgClSvc> sortedServiceLines = new TreeSet<StgClSvc>(serviceLines);

		for (StgClSvc stgClSvc : sortedServiceLines) {
			
			MedicalBehServiceLineType service  = new  MedicalBehServiceLineType();
			services.getServices().add(service);
			//Service Line Number			
			IdType serviceId = new IdType();
			service.setServiceNumber(serviceId);
			serviceId.setValue(getServiceLineNumber(claim.getOhiClNbr().trim(), stgClSvc));
			serviceId.setIdSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
			
			if(hasValue(stgClSvc.getOcsaClSufNbr())){
				
				service.setClaimSuffixNumber(stgClSvc.getOcsaClSufNbr().trim());
			}

			if(hasValue(stgClSvc.getMultSysCd())
					&& ClaimConstants.MULTISYSTEM_INDICATOR.equalsIgnoreCase(stgClSvc.getMultSysCd().trim())){
				service.setMultiSystemIndicator(stgClSvc.getMultSysCd().trim());
			}
			if(hasValue(stgClSvc.getHphcPerstClSvcNbr())){
				service.setPersistentServiceNumber(stgClSvc.getHphcPerstClSvcNbr().trim());
			}
			if(hasValue(stgClSvc.getHphcClSvcAdjCd())){
				service.setAdjustmentCode(stgClSvc.getHphcClSvcAdjCd().trim());
			}
			
			if(hasValue(stgClSvc.getAdjudClSvcDispCd())){
				MedicalBehServiceLineType.LifeCycleState  lStatus = new MedicalBehServiceLineType.LifeCycleState ();
				service.setLifeCycleState(lStatus);					
				lStatus.setCode(stgClSvc.getAdjudClSvcDispCd().trim());
				lStatus.setValue(stgClSvc.getAdjudClSvcDispCd().trim());
				lStatus.setCodeSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
			}
			//Paid indicator			
			MedicalBehServiceLineType.PaidIndicator  paidStatus = new MedicalBehServiceLineType.PaidIndicator();
			service.setPaidIndicator(paidStatus);						
			paidStatus.setCode(claimDecodeUtil.getohiMedicalPaidIndicator(stgClSvc.getPmtDt()));
			paidStatus.setValue(claimDecodeUtil.getohiMedicalPaidIndicator(stgClSvc.getPmtDt()));
			paidStatus.setCodeSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
			
			//Populate service line dates
			if(stgClSvc.getPmtDt() != null){				
				service.setFinalAdjudicationDate(XMLUtil.getXMLDate(stgClSvc.getPmtDt()));
			}

			if(stgClSvc.getSvcBegDt() != null){
				service.setServiceBeginDate(XMLUtil.getXMLDate(stgClSvc.getSvcBegDt()));
			}
			if(stgClSvc.getSvcEndDt() != null){
				service.setServiceEndDate(XMLUtil.getXMLDate(stgClSvc.getSvcEndDt()));
			}

			if(claim.getOhiInitClEntryDt() != null){
				service.setEnteredDate(XMLUtil.getXMLDate(claim.getOhiInitClEntryDt()));
			}

			if(claim.getHphcRcvDt()!=null){
				service.setReceiptDate(XMLUtil.getXMLDate(claim.getHphcRcvDt()));
			}

			if(stgClSvc.getOhiEventDt()!=null){
				service.setTransactionDate(XMLUtil.getXMLDate(stgClSvc.getOhiEventDt()));
			}
			
			if(stgClSvc.getOcsaUpdtDt() != null){
				service.setUpdateDate(XMLUtil.getXMLDate(stgClSvc.getOcsaUpdtDt()));
			}
			
			//Populate Product Offering.
			if(stgClSvc.getPdOfferCd() != null){
				MedicalBehServiceLineType.ProductOffering  prdOffering = new MedicalBehServiceLineType.ProductOffering ();
				service.setProductOffering(prdOffering);	
				prdOffering.setCode(stgClSvc.getPdOfferCd().trim());
				prdOffering.setCodeSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
				
				CodeSet pdOfferCode = codeDecodeUtil.getDecodedCode(Constants.CODE.PRODUCT_OFFERING,stgClSvc.getPdOfferCd().trim(),ClaimConstants.CLAIM_SOURCE.OHI);
				if(pdOfferCode != null && hasValue(pdOfferCode.getDescription())){
					prdOffering.setValue(pdOfferCode.getDescription());
				}	
			}
			//populate reimbursementMethod		
			if(hasValue(stgClSvc.getPrcOhiRmbusMthdTypCd())){
				MedicalBehServiceLineType.ReimbursementMethod reimbursementMethod = new MedicalBehServiceLineType.ReimbursementMethod();
				service.setReimbursementMethod(reimbursementMethod);
				reimbursementMethod.setCode(stgClSvc.getPrcOhiRmbusMthdTypCd().trim());
				//reimbursementMethod.setStringValue(stgClSvc.getPrcOhiRmbusMthdTypCd().trim());
				reimbursementMethod.setValue(codeDecodeUtil.decodeReimbursementMethodType(stgClSvc.getPrcOhiRmbusMthdTypCd().trim()));
				reimbursementMethod.setCodeSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
			}
			
			//populate feeLineQuantifier
			if(stgClSvc.getPrcOhiFeeQntfNbr() != null){
				service.setFeeLineQuantifier(stgClSvc.getPrcOhiFeeQntfNbr());
			}
			
			//added for cotiviti
			if(stgClSvc.getConsumerOfferNumber() != null){
				service.setConsumerOfferNumber(stgClSvc.getConsumerOfferNumber());
			}
			if(stgClSvc.getGroupNumber() != null){
				service.setGroupNumber(stgClSvc.getGroupNumber());
			}
			if(stgClSvc.getCustomerNumber() != null){
				service.setCustomerNumber(stgClSvc.getCustomerNumber());
			}
			if(stgClSvc.getExternalEditrReasonCodesText() != null){
				ExternalEditrReasonCodesText extEdiRsncd  = new ExternalEditrReasonCodesText();
				service.setExternalEditrReasonCodesText(extEdiRsncd);
				extEdiRsncd.setValue(stgClSvc.getExternalEditrReasonCodesText());				
			}
			if(stgClSvc.getOhiAllowUnitCount() != null){
				service.setOhiAllowUnitCount(new BigInteger(stgClSvc.getOhiAllowUnitCount().toString().trim()));
	
			}
			
			//Commented for more no of times unfinalized claim take more times issue R2:000021670647
			/*StgClSvcAudit stgClSvcAudit = null ;
			if(stgClSvc.getServiceaudits() != null && !stgClSvc.getServiceaudits().isEmpty() )
			{

				stgClSvcAudit = stgClSvc.getServiceaudits().iterator().next() ;	
				if(hasValue(stgClSvcAudit.getAuditCd()))
				{
					ValueType theAuditCode = service.addNewAuditCode();
					theAuditCode.setCode(stgClSvcAudit.getAuditCd().trim());
					theAuditCode.setStringValue(stgClSvcAudit.getAuditDesc());
					theAuditCode.setCodeSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
				}				

				if(hasValue(stgClSvcAudit.getVendorCd()))
				{
					ValueType theVendorCode = service.addNewVendorCode();
					theVendorCode.setCode(stgClSvcAudit.getVendorCd().trim());
					theVendorCode.setStringValue(stgClSvcAudit.getVendorDesc());
					theVendorCode.setCodeSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
				}
				
				if(hasValue(stgClSvcAudit.getReportingMsgCd()))
				{
					ValueType theReportingMessageCode = service.addNewReportingMessageCode();
					theReportingMessageCode.setCode(stgClSvcAudit.getReportingMsgCd().trim());
					theReportingMessageCode.setStringValue(stgClSvcAudit.getReportingMsgCd().trim());
					theReportingMessageCode.setCodeSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
				}
			}*/

			//Add medical service amounts
			medicalServiceLineAmounts(stgClSvc, service, responseCriteria.getResultContentSummary());

			//Add Diagnosis
			addServiceLineDiagnosis(stgClSvc, service,codeDecodeUtil);

			//procedure codes
			procedureCodeMedicalService(stgClSvc, service,codeDecodeUtil);			
			

			//Plan Design
			if(stgClSvc.getAdjudProdCd() != null){
				MedicalBehServiceLineType.PlanDesign boc = new MedicalBehServiceLineType.PlanDesign();
				service.setPlanDesign(boc);				
				boc.setCode(stgClSvc.getAdjudProdCd().trim());
				boc.setValue(stgClSvc.getAdjudProdCd().trim());
				boc.setCodeSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
			}

			//Product
			if(stgClSvc.getPrcProgNbr() != null){
				MedicalBehServiceLineType.Product product = new MedicalBehServiceLineType.Product();
				service.setProduct(product);
				product.setCode(stgClSvc.getPrcProgNbr().trim());
				product.setValue(stgClSvc.getPrcProgNbr().trim());
				product.setCodeSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
			}

			//Funding Arrangement
			if(stgClSvc.getFundArngTypCd() != null){
				MedicalBehServiceLineType.FundingArrangement fundingArrangement = new MedicalBehServiceLineType.FundingArrangement();
				service.setFundingArrangement(fundingArrangement);					
				fundingArrangement.setCode(stgClSvc.getFundArngTypCd().trim());
				fundingArrangement.setValue(stgClSvc.getFundArngTypCd().trim());
				fundingArrangement.setCodeSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
			}
			//Add providers
			
			providersMedicalService(stgClSvc, service, codeDecodeUtil);

			//Add authorization Numbers
			authorizationNumbersMedicalService(stgClSvc, service);

			//Add Inpatient Days Count
			if(hasValue(stgClSvc.getPrcProcUomCd()) && ClaimConstants.UOM_DAYS.equals(stgClSvc.getPrcProcUomCd().trim())){
				InpatientDayCounts inpatientDaysCnt = new InpatientDayCounts();
				service.setInpatientDayCounts(inpatientDaysCnt);
				InpatientDayCountType approvedInpatientDays = new InpatientDayCountType();
				inpatientDaysCnt.getInpatientDayCounts().add(approvedInpatientDays);
				approvedInpatientDays.setType(InpatientDayCountEnumType.APPROVED);			
				approvedInpatientDays.setValue(stgClSvc.getAdjudApprProcUnitCnt().toString());

				
				InpatientDayCountType billedInpatientDays = new InpatientDayCountType();
				inpatientDaysCnt.getInpatientDayCounts().add(billedInpatientDays);				
				billedInpatientDays.setType(InpatientDayCountEnumType.BILLED);
				billedInpatientDays.setValue(stgClSvc.getSubmProcUnitCnt().toString());

				
				InpatientDayCountType deniedInpatientDays = new InpatientDayCountType();
				inpatientDaysCnt.getInpatientDayCounts().add(deniedInpatientDays);					
				deniedInpatientDays.setType(InpatientDayCountEnumType.DENIED);
				deniedInpatientDays.setValue(stgClSvc.getAdjudDenyProcUnitCnt().toString());
			}

			//Add Counts
			addCounts(stgClSvc, service);

			//Add Minutes
			addMinutes(stgClSvc, service);

			//Add Minutes
			addModifiers(stgClSvc, service,codeDecodeUtil);

			//Add Adjudication Benefit Codes
			addAdjudicationBenefitCodes(stgClSvc, service);

			//Add Anesthesia Units
			if(hasValue(stgClSvc.getPrcProcUomCd()) && ClaimConstants.UOM_ANESTHESIA.equals(stgClSvc.getPrcProcUomCd().trim()))
				service.setUnits(stgClSvc.getClmdProcUnitCnt().setScale(4));

			//Location
			if(hasValue(stgClSvc.getPlaceOfSvcCd())){
				
				DecodeValueType location = new DecodeValueType();
				service.setPhysicalLocation(location);
				Code lCode = new Code();
				location.getCodes().add(lCode);
				lCode.setType(ClaimConstants.HPHC);
				String locDesc = codeDecodeUtil.decodeLocation(stgClSvc.getPlaceOfSvcCd().trim(),ClaimConstants.CLAIM_SOURCE.OHI) ;
				if(hasValue(locDesc)){
					lCode.setDescription(locDesc);
				}
				lCode.setValue(stgClSvc.getPlaceOfSvcCd().trim());
				lCode.setIndex(new BigInteger("0"));
			}
			//Fee Schedule
			if(hasValue(stgClSvc.getPrcFeeSchedCd())){
				MedicalBehServiceLineType.FeeSchedule feeSchedule = new MedicalBehServiceLineType.FeeSchedule();
				service.setFeeSchedule(feeSchedule);
				feeSchedule.setCode(stgClSvc.getPrcFeeSchedCd().trim());
				feeSchedule.setValue(stgClSvc.getPrcFeeSchedCd().trim());
				feeSchedule.setCodeSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
			}
			// Service Category 
			if(hasValue(stgClSvc.getOhiClSvcCatCd())){
				MedicalBehServiceLineType.ServiceCategory serviceCategory = new MedicalBehServiceLineType.ServiceCategory();
				service.setServiceCategory(serviceCategory);
				serviceCategory.setCode(stgClSvc.getOhiClSvcCatCd().trim());
				String svcCatDesc = codeDecodeUtil.decodeServiceCategory(stgClSvc.getOhiClSvcCatCd().trim(),ClaimConstants.CLAIM_SOURCE.OHI);
				if(hasValue(svcCatDesc)){
					serviceCategory.setValue(svcCatDesc.trim());
				}
				serviceCategory.setCodeSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
			}
			//Pay to
			if(hasValue(stgClSvc.getPrcPayeePayToCd())){				
				MedicalBehServiceLineType.PayTo payTo = new MedicalBehServiceLineType.PayTo();
				service.setPayTo(payTo);
				payTo.setCode(stgClSvc.getPrcPayeePayToCd().trim());
				String payToDesc = codeDecodeUtil.decodePayTo(stgClSvc.getPrcPayeePayToCd().trim());
				if(!hasValue(payToDesc)){
					payToDesc = stgClSvc.getPrcPayeePayToCd().trim();
				}
				payTo.setValue(payToDesc);
				if(hasValue(stgClSvc.getPayeeNbr())){
					payTo.setId(stgClSvc.getPayeeNbr().trim());
				}
				payTo.setCodeSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
			}

			//Add Pay Service Code
			if(hasValue(stgClSvc.getPrcPayClassCd())){
				MedicalBehServiceLineType.PayServiceCode payService= new MedicalBehServiceLineType.PayServiceCode();
				service.setPayServiceCode(payService);
				payService.setCode(stgClSvc.getPrcPayClassCd().trim());
				payService.setValue(stgClSvc.getPrcPayClassCd().trim());
				payService.setCodeSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
			}

			//Add filing limit Code
			if(stgClSvc.getPvCntrtFilLimDayCnt() != null){
				MedicalBehServiceLineType.FilingLimitDayCount filingLimit= new MedicalBehServiceLineType.FilingLimitDayCount();
				service.setFilingLimitDayCount(filingLimit);				
				filingLimit.setCode(stgClSvc.getPvCntrtFilLimDayCnt().toString());
				filingLimit.setValue(codeDecodeUtil.decodeFilingLimitDayCount(stgClSvc.getPvCntrtFilLimDayCnt().toString()));
				filingLimit.setCodeSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
			}

			if(hasValue(stgClSvc.getPmtNbr())){
				service.setOutboundPaymentNbr(stgClSvc.getPmtNbr().trim());
			}

			//Inner Tier Network
			if(hasValue(stgClSvc.getAdjudPvNtwrkCd())){
				MedicalBehServiceLineType.InnerTierNetwork innerTierNetwork= new MedicalBehServiceLineType.InnerTierNetwork();
				service.setInnerTierNetwork(innerTierNetwork);						
				innerTierNetwork.setCode(stgClSvc.getAdjudPvNtwrkCd().trim());
				innerTierNetwork.setValue(stgClSvc.getAdjudPvNtwrkCd().trim());
				innerTierNetwork.setCodeSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
			}

			//Adjudicated InNetwork Indicator 
			if(stgClSvc.getAdjudSvcInNtwrkInd() != null){
				MedicalBehServiceLineType.AdjudicatedInNetworkIndicator adjInNetworkIndicator= new MedicalBehServiceLineType.AdjudicatedInNetworkIndicator();
				service.setAdjudicatedInNetworkIndicator(adjInNetworkIndicator);						
				adjInNetworkIndicator.setCode(stgClSvc.getAdjudSvcInNtwrkInd().toString());
				adjInNetworkIndicator.setValue(stgClSvc.getAdjudSvcInNtwrkInd().toString());
				adjInNetworkIndicator.setCodeSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
			}

			
			// Add the service line version number
			if(stgClSvc.getOhiClVerNbr() > 0 )
			{
				service.setVersionNumber(BigInteger.valueOf(stgClSvc.getOhiClVerNbr()).toString()); 
			}
			
			//Reverseline Indicator 
			if(stgClSvc.getOcsaAdjCd() != null){
				MedicalBehServiceLineType.ReverseLineIndicator reverseLineIndicator= new MedicalBehServiceLineType.ReverseLineIndicator();
				service.setReverseLineIndicator(reverseLineIndicator);					
				reverseLineIndicator.setCode(claimDecodeUtil.getohiReverseLineIndicator(stgClSvc.getOcsaAdjCd()));
				reverseLineIndicator.setValue(claimDecodeUtil.getohiReverseLineIndicator(stgClSvc.getOcsaAdjCd()));
				reverseLineIndicator.setCodeSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
			}
			//Tier the claim is applying to
			if(hasValue(stgClSvc.getAdjudBnftTierCd())){
				MedicalBehServiceLineType.BenefitTierCode benefitTier= new MedicalBehServiceLineType.BenefitTierCode();
				service.setBenefitTierCode(benefitTier);				
				benefitTier.setCode(stgClSvc.getAdjudBnftTierCd().trim());
				benefitTier.setValue(stgClSvc.getAdjudBnftTierCd().trim());
				benefitTier.setCodeSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
			}
			
			//A HIPAA standard code that represents the financially responsible party.  
			//EXAMPLES: CO:Contractual Obligation
			//CR: Correction and Reversal
			//OA: Other Adjustment
			//PR: Patient Responsibility
			if(hasValue(stgClSvc.getSvcHippaGrp1Cd()) || hasValue(stgClSvc.getSvcHippaGrp2Cd()) || hasValue(stgClSvc.getSvcHippaGrp3Cd()) ||
			   hasValue(stgClSvc.getSvcHippaGrp4Cd()) || hasValue(stgClSvc.getSvcHippaGrp5Cd())	)
			{
				HippaGroupCodes hippaGroupCodes = new HippaGroupCodes();
				service.setHippaGroupCodes(hippaGroupCodes);

				if( hasValue(stgClSvc.getSvcHippaGrp1Cd()) )
				{
					HippaGroupCode theHippaGroupCode = new HippaGroupCode();
					hippaGroupCodes.getHippaGroupCodes().add(theHippaGroupCode);
					
					theHippaGroupCode.setCode(stgClSvc.getSvcHippaGrp1Cd().trim());
					theHippaGroupCode.setValue(stgClSvc.getSvcHippaGrp1Cd().trim());
					theHippaGroupCode.setCodeSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
					theHippaGroupCode.setIndex(new BigInteger("1"));
				}
				
				if( hasValue(stgClSvc.getSvcHippaGrp2Cd()) )
				{
					HippaGroupCode theHippaGroupCode = new HippaGroupCode();
					hippaGroupCodes.getHippaGroupCodes().add(theHippaGroupCode);
					
					theHippaGroupCode.setCode(stgClSvc.getSvcHippaGrp2Cd().trim());
					theHippaGroupCode.setValue(stgClSvc.getSvcHippaGrp2Cd().trim());
					theHippaGroupCode.setCodeSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
					theHippaGroupCode.setIndex(new BigInteger("2"));
				}
				
				if( hasValue(stgClSvc.getSvcHippaGrp3Cd()) )
				{
					HippaGroupCode theHippaGroupCode = new HippaGroupCode();
					hippaGroupCodes.getHippaGroupCodes().add(theHippaGroupCode);
					
					theHippaGroupCode.setCode(stgClSvc.getSvcHippaGrp3Cd().trim());
					theHippaGroupCode.setValue(stgClSvc.getSvcHippaGrp3Cd().trim());
					theHippaGroupCode.setCodeSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
					theHippaGroupCode.setIndex(new BigInteger("3"));
				}
				
				if( hasValue(stgClSvc.getSvcHippaGrp4Cd()) )
				{
					HippaGroupCode theHippaGroupCode = new HippaGroupCode();
					hippaGroupCodes.getHippaGroupCodes().add(theHippaGroupCode);
					
					theHippaGroupCode.setCode(stgClSvc.getSvcHippaGrp4Cd().trim());
					theHippaGroupCode.setValue(stgClSvc.getSvcHippaGrp4Cd().trim());
					theHippaGroupCode.setCodeSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
					theHippaGroupCode.setIndex(new BigInteger("4"));
				}
				
				if( hasValue(stgClSvc.getSvcHippaGrp5Cd()) )
				{
					HippaGroupCode theHippaGroupCode = new HippaGroupCode();
					hippaGroupCodes.getHippaGroupCodes().add(theHippaGroupCode);
					theHippaGroupCode.setCode(stgClSvc.getSvcHippaGrp5Cd().trim());
					theHippaGroupCode.setValue(stgClSvc.getSvcHippaGrp5Cd().trim());
					theHippaGroupCode.setCodeSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
					theHippaGroupCode.setIndex(new BigInteger("5"));
				}
				
			}
			
			
			
			//Add check details
			if(responseCriteria.getPaymentDetailFlag()){
				 addCheckDetails(stgClSvc, service, responseCriteria.getRelatedClaimsFlag(), claimInvoiceMap, deniedClaimInvoiceMap,apNoChkClaimInvoiceMap);
			}
			
			//Add internal messages
			addServiceInternalMessages(stgClSvc, service, serviceInternalMsgsMap, responseCriteria.getHipaaCodeFlag(),codeDecodeUtil);
			
			//Add audit for service line
			addServiceLinesAudit(stgClSvc, service, serviceAuditMap);
			
			 if(responseCriteria.getRemarksFlag()){
				 addServiceRemarks(stgClSvc, service);
			 }
		}
	}

	/**
	 * @param stgClsvc
	 * @param service
	 * 
	 * Add Service Line InternalMessages to response
	 * */
	 
	private void addServiceInternalMessages(StgClSvc stgClsvc, MedicalBehServiceLineType service, Map<Long, List<StgClSvcMsgPendXrefVw>> serviceInternalMsgsMap,boolean hipaaCodeFlag,CodeDecodeUtil codeDecodeUtil) {

		QueryBudgetContext qbc = LoggingThreadContext.getQueryBudgetContext();
		if(serviceInternalMsgsMap != null 
				&& serviceInternalMsgsMap.get(stgClsvc.getStgClSvcId()) != null){
			List<StgClSvcMsgPendXrefVw> intlMsgs = serviceInternalMsgsMap.get(stgClsvc.getStgClSvcId());
			if( intlMsgs != null){				
				InternalMessages internalMsgs  = new InternalMessages();
				service.setInternalMessages(internalMsgs);
				for(StgClSvcMsgPendXrefVw intlMsgXref : intlMsgs){
					if(intlMsgXref != null && intlMsgXref.getId() != null){
						if(intlMsgXref.getId().getOhiMsgPendCd() != null){
							
							InternalMessage internalMsg = new InternalMessage();
							internalMsgs.getInternalMessages().add(internalMsg);
							//Add source
							internalMsg.setSource(ClaimConstants.CLAIM_SOURCE.OHI.toString().toString());

							internalMsg.setSequenceNumber(BigInteger.valueOf(intlMsgXref.getSeqNbr()));
							//Add Code							
							InternalMessages.InternalMessage.Code code = new InternalMessages.InternalMessage.Code();
							internalMsg.setCode(code);
							if(hasValue(intlMsgXref.getId().getOhiMsgPendCd())){
								code.setCode(intlMsgXref.getId().getOhiMsgPendCd().trim());
							}
							if(intlMsgXref.getOhiMsgPend() != null){
								if(hasValue(intlMsgXref.getOhiMsgPend().getOhiMsgPendDsc())){
									code.setValue(intlMsgXref.getOhiMsgPend().getOhiMsgPendDsc().trim());
								}

								if(hasValue(intlMsgXref.getOhiMsgPend().getOhiExtnlMbDsc())){
									internalMsg.setMemberDescription(intlMsgXref.getOhiMsgPend().getOhiExtnlMbDsc().trim());
								}

								if(hasValue(intlMsgXref.getOhiMsgPend().getOhiExtnlPvDsc())){
									internalMsg.setProviderDescription(intlMsgXref.getOhiMsgPend().getOhiExtnlPvDsc().trim());
								}
							}
							if (hipaaCodeFlag || (ClaimConstants.OPERATION.GET_CLAIM_BY_MEMBERID == qbc
									.getOperationName())){			

								if(hasValue(intlMsgXref.getEdi277EntyCd())){

									InternalMessages.InternalMessage.Edi277EntityIdentifierCode entityCode = new InternalMessages.InternalMessage.Edi277EntityIdentifierCode();
									internalMsg.setEdi277EntityIdentifierCode(entityCode);
									entityCode.setCode(intlMsgXref.getEdi277EntyCd().trim());
									entityCode.setValue(intlMsgXref.getEdi277EntyCd().trim());
								}
								if(hasValue(intlMsgXref.getEdi277StdClmStatCd())){
									InternalMessages.InternalMessage.Edi277StatusCode status = new InternalMessages.InternalMessage.Edi277StatusCode();
									internalMsg.setEdi277StatusCode(status);									
									status.setCode(intlMsgXref.getEdi277StdClmStatCd().trim());
									String codeDesciption = codeDecodeUtil.decodeEdi277Status(intlMsgXref.getEdi277StdClmStatCd().trim(),ClaimConstants.CLAIM_SOURCE.OHI);
									if(!hasValue(codeDesciption)){
										codeDesciption = intlMsgXref.getEdi277StdClmStatCd().trim();
									}
									status.setValue(codeDesciption);
								}
								if(hasValue(intlMsgXref.getEdi277StdClmStatCatCd())){
									InternalMessages.InternalMessage.Edi277CategoryStatusCode status = new InternalMessages.InternalMessage.Edi277CategoryStatusCode();
									internalMsg.setEdi277CategoryStatusCode(status);											
									status.setCode(intlMsgXref.getEdi277StdClmStatCatCd().trim());
									String codeDesciption = codeDecodeUtil.decodeEdi277StatusCategory(intlMsgXref.getEdi277StdClmStatCatCd().trim(),ClaimConstants.CLAIM_SOURCE.OHI);
									if(!hasValue(codeDesciption)){
										codeDesciption = intlMsgXref.getEdi277StdClmStatCatCd().trim();
									}
									status.setValue(codeDesciption);
								}
							}

						}
					}
				}
			}
		}
	}
	
	/* Added service Lines audit to response*/
	private void addServiceLinesAudit(StgClSvc stgClsvc, MedicalBehServiceLineType service,Map<Long,StgClSvcAudit>serviceAuditMap){
		StgClSvcAudit stgClSvcAudit = null ;
		if(serviceAuditMap != null && serviceAuditMap.get(stgClsvc.getStgClSvcId()) != null){
			stgClSvcAudit = serviceAuditMap.get(stgClsvc.getStgClSvcId());			
			if(stgClSvcAudit != null)
			{
				if(hasValue(stgClSvcAudit.getAuditCd()))
				{
					MedicalBehServiceLineType.AuditCode theAuditCode = new MedicalBehServiceLineType.AuditCode();
					service.setAuditCode(theAuditCode);
					theAuditCode.setCode(stgClSvcAudit.getAuditCd().trim());
					theAuditCode.setValue(stgClSvcAudit.getAuditDesc());
					theAuditCode.setCodeSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
				}				

				if(hasValue(stgClSvcAudit.getVendorCd()))
				{
					MedicalBehServiceLineType.VendorCode theVendorCode = new MedicalBehServiceLineType.VendorCode();
					service.setVendorCode(theVendorCode);					
					theVendorCode.setCode(stgClSvcAudit.getVendorCd().trim());
					theVendorCode.setValue(stgClSvcAudit.getVendorDesc());
					theVendorCode.setCodeSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
				}
				
				if(hasValue(stgClSvcAudit.getReportingMsgCd()))
				{
					MedicalBehServiceLineType.ReportingMessageCode theReportingMessageCode = new MedicalBehServiceLineType.ReportingMessageCode();
					service.setReportingMessageCode(theReportingMessageCode);
					theReportingMessageCode.setCode(stgClSvcAudit.getReportingMsgCd().trim());
					theReportingMessageCode.setValue(stgClSvcAudit.getReportingMsgCd().trim());
					theReportingMessageCode.setCodeSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
				}
			}
		}
		
	}
	
	/**
	 * @param stgCls
	 * @return
	 * 
	 * Fetch Service line audits info
	 */
	private Map<Long, StgClSvcAudit> getServiceAudit(List<StgCl> stgCls){
		Set<Long> stgClSvcIds = new HashSet<Long>();

		for (StgCl stgCl : stgCls){
			for (StgClSvc stgClsvc : stgCl.getServices()){
				stgClSvcIds.add(stgClsvc.getStgClSvcId());
			}
		}
		
		return getServiceLineAuditInfo(stgClSvcIds);
	}
	
	/**
	 * @param stgClSvcIds
	 * 
	 * Fetch ServiceLine Audit Info
	 */
	private Map<Long, StgClSvcAudit> getServiceLineAuditInfo(Set<Long> stgClSvcIds){
		Map<Long, StgClSvcAudit> serviceLineAuditMap = null;
		List<StgClSvcAudit> serviceLineAudit = new ArrayList<StgClSvcAudit>();
		Set<Long> segmentStgClSvcIds = new HashSet<Long>();
		List<StgClSvcAudit> segRetrievedServiceLineAudit = null ;
		QueryBudgetContext qbc = LoggingThreadContext.getQueryBudgetContext();
		
		if(stgClSvcIds.size() > ClaimConstants.MAX_SQL_VALUES){
			
			// Multiple DB calls if service ids > 999
			int segCount = 0 ;
			int idCount = 0 ;
			
			for(Long stgClSvcId :stgClSvcIds)
			{
				segCount = segCount + 1 ;
				idCount = idCount + 1 ;

				segmentStgClSvcIds.add(stgClSvcId );
				if(segCount == ClaimConstants.MAX_SQL_VALUES || idCount == stgClSvcIds.size())
				{
					// retrieve the values from the db
					long starttime = System.currentTimeMillis();
					segRetrievedServiceLineAudit = internalMsgDao.getServiceLineAudit(segmentStgClSvcIds);		
					qbc.setOhiServiceLinesAuditQueryTime((System.currentTimeMillis() - starttime));
					
					// add to main list 
					for(StgClSvcAudit stgClSvcAudit:segRetrievedServiceLineAudit)
					{
						serviceLineAudit.add(stgClSvcAudit) ;
					}
					
					// reset for next seg
					segCount = 0 ;
					segRetrievedServiceLineAudit = null ;
				    segmentStgClSvcIds = new HashSet<Long>();
				}
			}
			
		} else{
			long starttime = System.currentTimeMillis();
			serviceLineAudit = internalMsgDao.getServiceLineAudit(stgClSvcIds);		
			qbc.setOhiServiceLinesAuditQueryTime((System.currentTimeMillis() - starttime));
		}
		
		if(serviceLineAudit != null){
			serviceLineAuditMap = new HashMap<Long, StgClSvcAudit>();
			for(StgClSvcAudit retrivedServiceLineAudit : serviceLineAudit){
				if(retrivedServiceLineAudit != null){
					serviceLineAuditMap.put(retrivedServiceLineAudit.getStgClSvcId(),retrivedServiceLineAudit);
					
					/*// Get the Audit for the service line
					List<StgClSvcAudit> ClSvcAudit = serviceLineAuditMap.get(retrivedServiceLineAudit.getStgClSvcId());
					
                    // if there is no collection of Audit for the service line create one
					if( ClSvcAudit == null){
						ClSvcAudit =  new ArrayList<StgClSvcAudit>();
						serviceLineAuditMap.put(retrivedServiceLineAudit.getStgClSvcId(), ClSvcAudit);
					}
					ClSvcAudit.add(retrivedServiceLineAudit);*/
				}
			}
		}
		return serviceLineAuditMap;	
	}

	/**
	 * @param stgCls
	 * @return
	 * 
	 * Fetch Claim and Service line Pend reasons and Internal messages
	 */
	private Map<Long, List<StgClSvcMsgPendXrefVw>> getClaimMessages(
			List<StgCl> stgCls) {

		Set<Long> stgClSvcIds = new HashSet<Long>();

		for (StgCl stgCl : stgCls){
			for (StgClSvc stgClsvc : stgCl.getServices()){
				stgClSvcIds.add(stgClsvc.getStgClSvcId());
			}
		}
		return getServiceLineInternalMessages(stgClSvcIds);
	}
	
	/**
	 * @param stgClSvcIds
	 * 
	 * Fetch Service line Internal Messages
	 */
	private Map<Long, List<StgClSvcMsgPendXrefVw>> getServiceLineInternalMessages(Set<Long> stgClSvcIds) {
		
		Map<Long, List<StgClSvcMsgPendXrefVw>> serviceInternalMsgsMap = null;
		QueryBudgetContext qbc = LoggingThreadContext.getQueryBudgetContext();
		long starttime = System.currentTimeMillis();
		
		List<StgClSvcMsgPendXrefVw> retrievedIntlMsgs = null ;
		List<StgClSvcMsgPendXrefVw> segRetrievedIntlMsgs = null ;
		// IF we have more ids than the query can accept we need to split into segments - this should not happen often
		Set<Long> segmentStgClSvcIds = new HashSet<Long>();
		if(stgClSvcIds.size() > ClaimConstants.MAX_SQL_VALUES)
		{
			retrievedIntlMsgs = new ArrayList<StgClSvcMsgPendXrefVw>();
			
			// we will have to make multiple calls - this does not happen very often
			int segCount = 0 ;
			int idCount = 0 ;
			for(Long stgClSvcId :stgClSvcIds)
			{
				segCount = segCount + 1 ;
				idCount = idCount + 1 ;

				segmentStgClSvcIds.add(stgClSvcId );
				if(segCount == ClaimConstants.MAX_SQL_VALUES || idCount == stgClSvcIds.size())
				{
					// retrieve the values from the db
					segRetrievedIntlMsgs = internalMsgDao.getInternalMessages(segmentStgClSvcIds);
					
					// add to main list 
					for(StgClSvcMsgPendXrefVw theStgClSvcMsgPendXrefVw:segRetrievedIntlMsgs)
					{
						retrievedIntlMsgs.add(theStgClSvcMsgPendXrefVw) ;
					}
					
					// reset for next seg
					segCount = 0 ;
					segRetrievedIntlMsgs = null ;
				    segmentStgClSvcIds = new HashSet<Long>();
				}
			}
		}
		else
		{
			retrievedIntlMsgs = internalMsgDao.getInternalMessages(stgClSvcIds);
		}

		qbc.setOhiIntrnlMsgQueryTime((System.currentTimeMillis() - starttime));
		if(retrievedIntlMsgs != null){
			serviceInternalMsgsMap = new HashMap<Long, List<StgClSvcMsgPendXrefVw>>();
			for(StgClSvcMsgPendXrefVw retrievedIntlMsg : retrievedIntlMsgs){
				if(retrievedIntlMsg != null && retrievedIntlMsg.getId() != null){
					
					// Get the internal messages for the service line
					List<StgClSvcMsgPendXrefVw> mapInternalMsgs = serviceInternalMsgsMap.get(retrievedIntlMsg.getId().getStgClSvcId());
					
                    // if there is no collection of messages for the service line create one
					if( mapInternalMsgs == null){
						mapInternalMsgs =  new ArrayList<StgClSvcMsgPendXrefVw>();
						serviceInternalMsgsMap.put(retrievedIntlMsg.getId().getStgClSvcId(), mapInternalMsgs);
					}
					
					boolean addCode = true ;
					// we only want one entry per message code (ohiMsgPendCd) - this is a list not a map
					// if the current code already has been added do not add again
					for(StgClSvcMsgPendXrefVw mapInternalMsg :mapInternalMsgs)
					{    
						// If we already have a code don't duplicate
						if( mapInternalMsg.getId().getOhiMsgPendCd().equalsIgnoreCase(retrievedIntlMsg.getId().getOhiMsgPendCd()) &&
							mapInternalMsg.getSeqNbr() == retrievedIntlMsg.getSeqNbr()	)
						{
							addCode = false ;
						}
					}
					
					if(addCode)
					{
						mapInternalMsgs.add(retrievedIntlMsg);						
					}
				}
			}
		}
		return serviceInternalMsgsMap;
	}
	
	/*
	 * Populate unfinalized reason codes
	 * @param stgCl
	 * @param mBEHClaim
	 * @param codeDecodeUtil
	 */
	 
	private void addUnfinalizedreasonCodes(StgCl stgCl,
			MedicalBehClaimType mBEHClaim,CodeDecodeUtil codeDecodeUtil) {

		if(hasUnfinalizedReasonValue(stgCl.getOhiClUnfnlzRsn1Cd()) 
				||hasUnfinalizedReasonValue(stgCl.getOhiClUnfnlzRsn2Cd()) 
				|| hasUnfinalizedReasonValue(stgCl.getOhiClUnfnlzRsn3Cd()))
		{
			
			UnfinalizedReasons unfinalizedRsns = new UnfinalizedReasons();
			mBEHClaim.setUnfinalizedReasons(unfinalizedRsns);
			if(hasUnfinalizedReasonValue(stgCl.getOhiClUnfnlzRsn1Cd())){				
				MedicalBehClaimType.UnfinalizedReasons.UnfinalizedReason reasonCode = new MedicalBehClaimType.UnfinalizedReasons.UnfinalizedReason();
				unfinalizedRsns.getUnfinalizedReasons().add(reasonCode);
				reasonCode.setCode(stgCl.getOhiClUnfnlzRsn1Cd().trim());
				reasonCode.setValue(codeDecodeUtil.decodeUnfinalizedReasonCode(stgCl.getOhiClUnfnlzRsn1Cd().trim()));
				reasonCode.setCodeSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
			}
			if(hasUnfinalizedReasonValue(stgCl.getOhiClUnfnlzRsn2Cd())){
				MedicalBehClaimType.UnfinalizedReasons.UnfinalizedReason reasonCode = new MedicalBehClaimType.UnfinalizedReasons.UnfinalizedReason();
				unfinalizedRsns.getUnfinalizedReasons().add(reasonCode);				
				reasonCode.setCode(stgCl.getOhiClUnfnlzRsn2Cd().trim());
				reasonCode.setValue(codeDecodeUtil.decodeUnfinalizedReasonCode(stgCl.getOhiClUnfnlzRsn2Cd().trim()));
				reasonCode.setCodeSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
			}
			if(hasUnfinalizedReasonValue(stgCl.getOhiClUnfnlzRsn3Cd())){
				MedicalBehClaimType.UnfinalizedReasons.UnfinalizedReason reasonCode = new MedicalBehClaimType.UnfinalizedReasons.UnfinalizedReason();
				unfinalizedRsns.getUnfinalizedReasons().add(reasonCode);				
				reasonCode.setCode(stgCl.getOhiClUnfnlzRsn3Cd().trim());
				reasonCode.setValue(codeDecodeUtil.decodeUnfinalizedReasonCode(stgCl.getOhiClUnfnlzRsn3Cd().trim()));
				reasonCode.setCodeSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
			}
		}
	}
	
	protected boolean hasUnfinalizedReasonValue(String value){
		if(value == null || "".equals(value.trim())){
			return false;
		}
		
		// Only return true if one of the approved unfinalized reason codes  from the application context
		if(claimResultCodeHandler.getAllowedUnfinalixedCodes().contains(value))
		{   
			return true;
		}
		else
		{
			return false;
		}
		
	}
		

	/**
	 * @param claim
	 * @param mBEHClaim
	 */
	private void addUBBillType(StgCl claim, MedicalBehClaimType mBEHClaim,CodeDecodeUtil codeDecodeUtil) {

		if(hasValue(claim.getUbBillTypCd())){
			UbBillType billType = new UbBillType();
			mBEHClaim.setUbBillType(billType);			
			UbBillType.Code billTypeCd = new UbBillType.Code();
			billType.setCode(billTypeCd);
			billTypeCd.setCode(claim.getUbBillTypCd().trim());
			billTypeCd.setValue(codeDecodeUtil.decodeBillTypeCode(claim.getUbBillTypCd().trim()));
			billTypeCd.setCodeSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
			billTypeCd.setType(ClaimConstants.HIPAA);
		}

	}

	/**
	 * @param claim
	 * @param mBEHClaim
	 */
	private void addClaimCategoryStatusCodes(StgCl claim,
			MedicalBehClaimType mBEHClaim) {

		if(hasValue(claim.getEdi277StdClmStatCatCd())){
			MedicalBehClaimType.Edi277CategoryStatusCode catStatus = new MedicalBehClaimType.Edi277CategoryStatusCode();
			mBEHClaim.setEdi277CategoryStatusCode(catStatus);
			catStatus.setIndex(new BigInteger("1"));
			catStatus.setCode(claim.getEdi277StdClmStatCatCd().trim());
			catStatus.setValue(claim.getEdi277StdClmStatCatCd().trim());
			catStatus.setCodeSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
			catStatus.setType(ClaimConstants.HIPAA);
		}
	}

	/**
	 * @param claim
	 * @param mBEHClaim
	 */
	private void addClaimStatusCodes(StgCl claim, MedicalBehClaimType mBEHClaim) {

		if(hasValue(claim.getEdi277StdClmStatCd())){
			MedicalBehClaimType.Edi277StatusCode status = new MedicalBehClaimType.Edi277StatusCode();
			mBEHClaim.setEdi277StatusCode(status);
			status.setIndex(new BigInteger("1"));
			status.setCode(claim.getEdi277StdClmStatCd().trim());
			status.setValue(claim.getEdi277StdClmStatCd().trim());
			status.setCodeSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
			status.setType(ClaimConstants.HIPAA);
		}
	}

	/**
	 * @param claim
	 * @return max of service paid date 
	 */
	private Date getClaimAdjudicationDate(StgCl claim) {
		Date paidDate = null;
		//for XRM PaidDate/FinalAdjudicationDate, taking latest date from service lines into claim level
		List<StgClSvc> serviceLines = new ArrayList<StgClSvc>(claim.getServices());
		Collections.sort(serviceLines, new OhiMedicalServiceLinePaidDateComparator());
		if(serviceLines.get(serviceLines.size()-1) != null && serviceLines.get(serviceLines.size()-1).getPmtDt() != null){			
			paidDate = serviceLines.get(serviceLines.size()-1).getPmtDt();
		}
		return paidDate;
	}
	
	/**
	 * @param claim
	 * @return min of service end date 
	 */
	private Date getClaimServiceBeginDate(StgCl claim) {
		Date startDate = null;
		//for XRM Service Begin Date, taking least date from service lines into claim level
		List<StgClSvc> serviceLines = new ArrayList<StgClSvc>(claim.getServices());
		Collections.sort(serviceLines, new OhiMedicalServiceLineEffectiveDateComparator());
		if(serviceLines.get(0) != null && serviceLines.get(0).getSvcBegDt() != null){			
			startDate = serviceLines.get(0).getSvcBegDt();
		}
		return startDate;
	}
	
	/**
	 * @param claim
	 * @return max of service end date 
	 */
	private Date getClaimServiceEndDate(StgCl claim) {
		Date endDate = null;
		//for XRM Service End Date, taking latest date from service lines into claim level
		List<StgClSvc> serviceLines = new ArrayList<StgClSvc>(claim.getServices());
		Collections.sort(serviceLines, new OhiMedicalServiceLineEndDateComparator());
		if(serviceLines.get(serviceLines.size()-1) != null && serviceLines.get(serviceLines.size()-1).getSvcEndDt() != null){
			endDate = serviceLines.get(serviceLines.size()-1).getSvcEndDt();
		}
		return endDate;
	}
	
	/**
	 * @param claim
	 * @param mBEHClaim
	 * @param codeDecodeUtil
	 */
	private void addClaimProcedureCodes(StgCl claim,
			MedicalBehClaimType mBEHClaim, CodeDecodeUtil codeDecodeUtil) {
		
		String codeSubStype = null;
		
		if(hasValue(claim.getIcdProc1Cd()) || hasValue(claim.getIcdProc2Cd()) ||
				hasValue(claim.getIcdProc3Cd()) || hasValue(claim.getIcdProc4Cd()) ||
				hasValue(claim.getIcdProc5Cd()) || hasValue(claim.getIcdProc6Cd()) ||
				hasValue(claim.getIcdProc7Cd()) || hasValue(claim.getIcdProc8Cd()) ||
				hasValue(claim.getIcdProc9Cd()) || hasValue(claim.getIcdProc10Cd()) ||
				hasValue(claim.getIcdProc11Cd()) || hasValue(claim.getIcdProc12Cd()) ||
				hasValue(claim.getIcdProc13Cd()) || hasValue(claim.getIcdProc14Cd()) ||
				hasValue(claim.getIcdProc15Cd()) || hasValue(claim.getIcdProc16Cd()) ||
				hasValue(claim.getIcdProc17Cd()) || hasValue(claim.getIcdProc18Cd()) ||
				hasValue(claim.getIcdProc19Cd()) || hasValue(claim.getIcdProc20Cd()) ||
				hasValue(claim.getIcdProc21Cd()) || hasValue(claim.getIcdProc22Cd()) ||
				hasValue(claim.getIcdProc23Cd()) || hasValue(claim.getIcdProc24Cd()) ||
				hasValue(claim.getIcdProc25Cd()) || hasValue(claim.getIcdProc26Cd())){
			
			Procedures procedures = new Procedures();
			mBEHClaim.setProcedures(procedures);
			
			Date sStartDate = null;
			Date serviceStartDate = null;
			sStartDate = getClaimServiceBeginDate(claim);
			if (sStartDate != null) 
				serviceStartDate = sStartDate;
			else 
				serviceStartDate = new Date();
			
			if(claim.getClIcdRevisNbr() != null){
				codeSubStype = "ICD-" + claim.getClIcdRevisNbr();
			}
			if(hasValue(claim.getIcdProc1Cd())){
				
				Procedure pCode = new Procedure();
				procedures.getProcedures().add(pCode);				 
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				pCode.getCodes().add(code);
				code.setValue(claim.getIcdProc1Cd().trim());
				code.setIndex(new BigInteger("1")); 
				code.setType("HIPAA");
				
				if(codeSubStype != null){
					code.setSubType(codeSubStype);
				}
				CodeSet procCode = codeDecodeUtil.getDecodedCodeProcedure(Constants.CODE.PROCEDURE,claim.getIcdProc1Cd().trim(),serviceStartDate,ClaimConstants.CLAIM_SOURCE.OHI);
				if(procCode != null && hasValue(procCode.getDescription())){
					code.setDescription(procCode.getDescription());
				}
				if(procCode != null && hasValue(procCode.getShortDescription())){
					code.setShortDescription(procCode.getShortDescription());
				}
				code.setSource(ClaimConstants.CLAIM_SOURCE.OHI.toString().toString());				
				//adding procedure date
				if(claim.getIcdProc1Dt () !=  null){					
					pCode.setProcedureDate(XMLUtil.getXMLDate(claim.getIcdProc1Dt()));
				}				
				
			}
			if(hasValue(claim.getIcdProc2Cd())){
				Procedure pCode = new Procedure();
				procedures.getProcedures().add(pCode);
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				pCode.getCodes().add(code);				
				code.setValue(claim.getIcdProc2Cd().trim());
				code.setIndex(new BigInteger("2"));
				code.setType("HIPAA");
				if(codeSubStype != null){
					code.setSubType(codeSubStype);
				}
				CodeSet procCode = codeDecodeUtil.getDecodedCodeProcedure(Constants.CODE.PROCEDURE,claim.getIcdProc2Cd().trim(),serviceStartDate,ClaimConstants.CLAIM_SOURCE.OHI);
				if(procCode != null && hasValue(procCode.getDescription())){
					code.setDescription(procCode.getDescription());
				}
				if(procCode != null && hasValue(procCode.getShortDescription())){
					code.setShortDescription(procCode.getShortDescription());
				}
				code.setSource(ClaimConstants.CLAIM_SOURCE.OHI.toString().toString());
				//adding procedure date
				if(claim.getIcdProc2Dt () !=  null){					
					pCode.setProcedureDate(XMLUtil.getXMLDate(claim.getIcdProc2Dt()));
				}
			}
			if(hasValue(claim.getIcdProc3Cd())){
				Procedure pCode = new Procedure();
				procedures.getProcedures().add(pCode);
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				pCode.getCodes().add(code);						
				code.setValue(claim.getIcdProc3Cd().trim());
				code.setIndex(new BigInteger("3"));
				code.setType("HIPAA");
				if(codeSubStype != null){
					code.setSubType(codeSubStype);
				}
				CodeSet procCode = codeDecodeUtil.getDecodedCodeProcedure(Constants.CODE.PROCEDURE,claim.getIcdProc3Cd().trim(),serviceStartDate,ClaimConstants.CLAIM_SOURCE.OHI);
				if(procCode != null && hasValue(procCode.getDescription())){
					code.setDescription(procCode.getDescription());
				}
				if(procCode != null && hasValue(procCode.getShortDescription())){
					code.setShortDescription(procCode.getShortDescription());
				}
				code.setSource(ClaimConstants.CLAIM_SOURCE.OHI.toString().toString());
				//adding procedure date
				if(claim.getIcdProc3Dt () !=  null){
					pCode.setProcedureDate(XMLUtil.getXMLDate(claim.getIcdProc3Dt()));
				}
			}
			if(hasValue(claim.getIcdProc4Cd())){
				Procedure pCode = new Procedure();
				procedures.getProcedures().add(pCode);
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				pCode.getCodes().add(code);				
				code.setValue(claim.getIcdProc4Cd().trim());
				code.setIndex(new BigInteger("4"));
				code.setType("HIPAA");
				if(codeSubStype != null){
					code.setSubType(codeSubStype);
				}
				CodeSet procCode = codeDecodeUtil.getDecodedCodeProcedure(Constants.CODE.PROCEDURE,claim.getIcdProc4Cd().trim(),serviceStartDate,ClaimConstants.CLAIM_SOURCE.OHI);
				if(procCode != null && hasValue(procCode.getDescription())){
					code.setDescription(procCode.getDescription());
				}
				if(procCode != null && hasValue(procCode.getShortDescription())){
					code.setShortDescription(procCode.getShortDescription());
				}
				code.setSource(ClaimConstants.CLAIM_SOURCE.OHI.toString().toString());
				//adding procedure date
				if(claim.getIcdProc4Dt () !=  null){					
					pCode.setProcedureDate(XMLUtil.getXMLDate(claim.getIcdProc4Dt()));
				}
			}
			if(hasValue(claim.getIcdProc5Cd())){
				Procedure pCode = new Procedure();
				procedures.getProcedures().add(pCode);
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				pCode.getCodes().add(code);		
				code.setValue(claim.getIcdProc5Cd().trim());
				code.setIndex(new BigInteger("5"));
				code.setType("HIPAA");
				if(codeSubStype != null){
					code.setSubType(codeSubStype);
				}
				CodeSet procCode = codeDecodeUtil.getDecodedCodeProcedure(Constants.CODE.PROCEDURE,claim.getIcdProc5Cd().trim(),serviceStartDate,ClaimConstants.CLAIM_SOURCE.OHI);
				if(procCode != null && hasValue(procCode.getDescription())){
					code.setDescription(procCode.getDescription());
				}
				if(procCode != null && hasValue(procCode.getShortDescription())){
					code.setShortDescription(procCode.getShortDescription());
				}
				code.setSource(ClaimConstants.CLAIM_SOURCE.OHI.toString().toString());
				//adding procedure date
				if(claim.getIcdProc5Dt () !=  null){
					pCode.setProcedureDate(XMLUtil.getXMLDate(claim.getIcdProc5Dt()));
				}
			}
			if(hasValue(claim.getIcdProc6Cd())){
				////Procedure pCode = procedures.addNewProcedure();
				Procedure pCode = new Procedure();
				procedures.getProcedures().add(pCode);
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				pCode.getCodes().add(code);					
				code.setValue(claim.getIcdProc6Cd().trim());
				code.setIndex(new BigInteger("6"));
				code.setType("HIPAA");
				if(codeSubStype != null){
					code.setSubType(codeSubStype);
				}
				CodeSet procCode = codeDecodeUtil.getDecodedCodeProcedure(Constants.CODE.PROCEDURE,claim.getIcdProc6Cd().trim(),serviceStartDate,ClaimConstants.CLAIM_SOURCE.OHI);
				if(procCode != null && hasValue(procCode.getDescription())){
					code.setDescription(procCode.getDescription());
				}
				if(procCode != null && hasValue(procCode.getShortDescription())){
					code.setShortDescription(procCode.getShortDescription());
				}
				code.setSource(ClaimConstants.CLAIM_SOURCE.OHI.toString().toString());
				//adding procedure date
				if(claim.getIcdProc6Dt () !=  null){
					pCode.setProcedureDate(XMLUtil.getXMLDate(claim.getIcdProc6Dt()));
				}
			}
			if(hasValue(claim.getIcdProc7Cd())){
				Procedure pCode = new Procedure();
				procedures.getProcedures().add(pCode);
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				pCode.getCodes().add(code);				
				code.setValue(claim.getIcdProc7Cd().trim());
				code.setIndex(new BigInteger("7"));
				code.setType("HIPAA");
				if(codeSubStype != null){
					code.setSubType(codeSubStype);
				}
				CodeSet procCode = codeDecodeUtil.getDecodedCodeProcedure(Constants.CODE.PROCEDURE,claim.getIcdProc7Cd().trim(),serviceStartDate,ClaimConstants.CLAIM_SOURCE.OHI);
				if(procCode != null && hasValue(procCode.getDescription())){
					code.setDescription(procCode.getDescription());
				}
				if(procCode != null && hasValue(procCode.getShortDescription())){
					code.setShortDescription(procCode.getShortDescription());
				}
				code.setSource(ClaimConstants.CLAIM_SOURCE.OHI.toString().toString());
				//adding procedure date
				if(claim.getIcdProc7Dt () !=  null){
					pCode.setProcedureDate(XMLUtil.getXMLDate(claim.getIcdProc7Dt()));
				}
			}
			if(hasValue(claim.getIcdProc8Cd())){
				Procedure pCode = new Procedure();
				procedures.getProcedures().add(pCode);
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				pCode.getCodes().add(code);					
				code.setValue(claim.getIcdProc8Cd().trim());
				code.setIndex(new BigInteger("8"));
				code.setType("HIPAA");
				if(codeSubStype != null){
					code.setSubType(codeSubStype);
				}
				CodeSet procCode = codeDecodeUtil.getDecodedCodeProcedure(Constants.CODE.PROCEDURE,claim.getIcdProc8Cd().trim(),serviceStartDate,ClaimConstants.CLAIM_SOURCE.OHI);
				if(procCode != null && hasValue(procCode.getDescription())){
					code.setDescription(procCode.getDescription());
				}
				if(procCode != null && hasValue(procCode.getShortDescription())){
					code.setShortDescription(procCode.getShortDescription());
				}
				code.setSource(ClaimConstants.CLAIM_SOURCE.OHI.toString().toString());
				//adding procedure date
				if(claim.getIcdProc8Dt () !=  null){
					pCode.setProcedureDate(XMLUtil.getXMLDate(claim.getIcdProc8Dt()));
				}
			}
			if(hasValue(claim.getIcdProc9Cd())){
				Procedure pCode = new Procedure();
				procedures.getProcedures().add(pCode);
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				pCode.getCodes().add(code);				
				code.setValue(claim.getIcdProc9Cd().trim());
				code.setIndex(new BigInteger("9"));
				code.setType("HIPAA");
				code.setSubType(codeSubStype);if(codeSubStype != null){
					code.setSubType(codeSubStype);
				}
				CodeSet procCode = codeDecodeUtil.getDecodedCodeProcedure(Constants.CODE.PROCEDURE,claim.getIcdProc9Cd().trim(),serviceStartDate,ClaimConstants.CLAIM_SOURCE.OHI);
				if(procCode != null && hasValue(procCode.getDescription())){
					code.setDescription(procCode.getDescription());
				}
				if(procCode != null && hasValue(procCode.getShortDescription())){
					code.setShortDescription(procCode.getShortDescription());
				}
				code.setSource(ClaimConstants.CLAIM_SOURCE.OHI.toString().toString());
				//adding procedure date
				if(claim.getIcdProc9Dt () !=  null){
					pCode.setProcedureDate(XMLUtil.getXMLDate(claim.getIcdProc9Dt()));					
				}
			}
			if(hasValue(claim.getIcdProc10Cd())){
				Procedure pCode = new Procedure();
				procedures.getProcedures().add(pCode);
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				pCode.getCodes().add(code);					
				code.setValue(claim.getIcdProc10Cd().trim());
				code.setIndex(new BigInteger("10"));
				code.setType("HIPAA");
				if(codeSubStype != null){
					code.setSubType(codeSubStype);
				}
				CodeSet procCode = codeDecodeUtil.getDecodedCodeProcedure(Constants.CODE.PROCEDURE,claim.getIcdProc10Cd().trim(),serviceStartDate,ClaimConstants.CLAIM_SOURCE.OHI);
				if(procCode != null && hasValue(procCode.getDescription())){
					code.setDescription(procCode.getDescription());
				}
				if(procCode != null && hasValue(procCode.getShortDescription())){
					code.setShortDescription(procCode.getShortDescription());
				}
				code.setSource(ClaimConstants.CLAIM_SOURCE.OHI.toString().toString());
				//adding procedure date
				if(claim.getIcdProc10Dt () !=  null){
					pCode.setProcedureDate(XMLUtil.getXMLDate(claim.getIcdProc10Dt()));					
				}
			}
			if(hasValue(claim.getIcdProc11Cd())){
				Procedure pCode = new Procedure();
				procedures.getProcedures().add(pCode);
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				pCode.getCodes().add(code);					
				code.setValue(claim.getIcdProc11Cd().trim());
				code.setIndex(new BigInteger("11"));
				code.setType("HIPAA");
				if(codeSubStype != null){
					code.setSubType(codeSubStype);
				}
				CodeSet procCode = codeDecodeUtil.getDecodedCodeProcedure(Constants.CODE.PROCEDURE,claim.getIcdProc11Cd().trim(),serviceStartDate,ClaimConstants.CLAIM_SOURCE.OHI);
				if(procCode != null && hasValue(procCode.getDescription())){
					code.setDescription(procCode.getDescription());
				}
				if(procCode != null && hasValue(procCode.getShortDescription())){
					code.setShortDescription(procCode.getShortDescription());
				}
				code.setSource(ClaimConstants.CLAIM_SOURCE.OHI.toString().toString());
				//adding procedure date
				if(claim.getIcdProc11Dt () !=  null){
					pCode.setProcedureDate(XMLUtil.getXMLDate(claim.getIcdProc11Dt()));						
				}
			}
			if(hasValue(claim.getIcdProc12Cd())){
				Procedure pCode = new Procedure();
				procedures.getProcedures().add(pCode);
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				pCode.getCodes().add(code);						
				code.setValue(claim.getIcdProc12Cd().trim());
				code.setIndex(new BigInteger("12"));
				code.setType("HIPAA");
				if(codeSubStype != null){
					code.setSubType(codeSubStype);
				}
				CodeSet procCode = codeDecodeUtil.getDecodedCodeProcedure(Constants.CODE.PROCEDURE,claim.getIcdProc12Cd().trim(),serviceStartDate,ClaimConstants.CLAIM_SOURCE.OHI);
				if(procCode != null && hasValue(procCode.getDescription())){
					code.setDescription(procCode.getDescription());
				}
				if(procCode != null && hasValue(procCode.getShortDescription())){
					code.setShortDescription(procCode.getShortDescription());
				}
				code.setSource(ClaimConstants.CLAIM_SOURCE.OHI.toString().toString());
				//adding procedure date
				if(claim.getIcdProc12Dt () !=  null){
					pCode.setProcedureDate(XMLUtil.getXMLDate(claim.getIcdProc12Dt()));						
				}
			}
			if(hasValue(claim.getIcdProc13Cd())){
				Procedure pCode = new Procedure();
				procedures.getProcedures().add(pCode);
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				pCode.getCodes().add(code);					
				code.setValue(claim.getIcdProc13Cd().trim());
				code.setIndex(new BigInteger("13"));
				code.setType("HIPAA");
				if(codeSubStype != null){
					code.setSubType(codeSubStype);
				}
				CodeSet procCode = codeDecodeUtil.getDecodedCodeProcedure(Constants.CODE.PROCEDURE,claim.getIcdProc13Cd().trim(),serviceStartDate,ClaimConstants.CLAIM_SOURCE.OHI);
				if(procCode != null && hasValue(procCode.getDescription())){
					code.setDescription(procCode.getDescription());
				}
				if(procCode != null && hasValue(procCode.getShortDescription())){
					code.setShortDescription(procCode.getShortDescription());
				}
				code.setSource(ClaimConstants.CLAIM_SOURCE.OHI.toString().toString());
				//adding procedure date
				if(claim.getIcdProc13Dt () !=  null){
					pCode.setProcedureDate(XMLUtil.getXMLDate(claim.getIcdProc13Dt()));							
				}
			}
			if(hasValue(claim.getIcdProc14Cd())){
				Procedure pCode = new Procedure();
				procedures.getProcedures().add(pCode);
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				pCode.getCodes().add(code);	
				code.setValue(claim.getIcdProc14Cd().trim());
				code.setIndex(new BigInteger("14"));
				code.setType("HIPAA");
				if(codeSubStype != null){
					code.setSubType(codeSubStype);
				}
				CodeSet procCode = codeDecodeUtil.getDecodedCodeProcedure(Constants.CODE.PROCEDURE,claim.getIcdProc14Cd().trim(),serviceStartDate,ClaimConstants.CLAIM_SOURCE.OHI);
				if(procCode != null && hasValue(procCode.getDescription())){
					code.setDescription(procCode.getDescription());
				}
				if(procCode != null && hasValue(procCode.getShortDescription())){
					code.setShortDescription(procCode.getShortDescription());
				}
				code.setSource(ClaimConstants.CLAIM_SOURCE.OHI.toString().toString());
				//adding procedure date
				if(claim.getIcdProc14Dt () !=  null){
					pCode.setProcedureDate(XMLUtil.getXMLDate(claim.getIcdProc14Dt()));						
				}
			}
			if(hasValue(claim.getIcdProc15Cd())){
				Procedure pCode = new Procedure();
				procedures.getProcedures().add(pCode);
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				pCode.getCodes().add(code);					
				code.setValue(claim.getIcdProc15Cd().trim());
				code.setIndex(new BigInteger("15"));
				code.setType("HIPAA");
				if(codeSubStype != null){
					code.setSubType(codeSubStype);
				}
				CodeSet procCode = codeDecodeUtil.getDecodedCodeProcedure(Constants.CODE.PROCEDURE,claim.getIcdProc15Cd().trim(),serviceStartDate,ClaimConstants.CLAIM_SOURCE.OHI);
				if(procCode != null && hasValue(procCode.getDescription())){
					code.setDescription(procCode.getDescription());
				}
				if(procCode != null && hasValue(procCode.getShortDescription())){
					code.setShortDescription(procCode.getShortDescription());
				}
				code.setSource(ClaimConstants.CLAIM_SOURCE.OHI.toString().toString());
				//adding procedure date
				if(claim.getIcdProc15Dt () !=  null){
					pCode.setProcedureDate(XMLUtil.getXMLDate(claim.getIcdProc15Dt()));
				}
			}
			if(hasValue(claim.getIcdProc16Cd())){
				Procedure pCode = new Procedure();
				procedures.getProcedures().add(pCode);
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				pCode.getCodes().add(code);						
				code.setValue(claim.getIcdProc16Cd().trim());
				code.setIndex(new BigInteger("16"));
				code.setType("HIPAA");
				if(codeSubStype != null){
					code.setSubType(codeSubStype);
				}
				CodeSet procCode = codeDecodeUtil.getDecodedCodeProcedure(Constants.CODE.PROCEDURE,claim.getIcdProc16Cd().trim(),serviceStartDate,ClaimConstants.CLAIM_SOURCE.OHI);
				if(procCode != null && hasValue(procCode.getDescription())){
					code.setDescription(procCode.getDescription());
				}
				if(procCode != null && hasValue(procCode.getShortDescription())){
					code.setShortDescription(procCode.getShortDescription());
				}
				code.setSource(ClaimConstants.CLAIM_SOURCE.OHI.toString().toString());
				//adding procedure date
				if(claim.getIcdProc16Dt () !=  null){
					pCode.setProcedureDate(XMLUtil.getXMLDate(claim.getIcdProc16Dt()));
				}
			}
			if(hasValue(claim.getIcdProc17Cd())){
				Procedure pCode = new Procedure();
				procedures.getProcedures().add(pCode);
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				pCode.getCodes().add(code);					
				code.setValue(claim.getIcdProc17Cd().trim());
				code.setIndex(new BigInteger("17"));
				code.setType("HIPAA");
				if(codeSubStype != null){
					code.setSubType(codeSubStype);
				}
				CodeSet procCode = codeDecodeUtil.getDecodedCodeProcedure(Constants.CODE.PROCEDURE,claim.getIcdProc17Cd().trim(),serviceStartDate,ClaimConstants.CLAIM_SOURCE.OHI);
				if(procCode != null && hasValue(procCode.getDescription())){
					code.setDescription(procCode.getDescription());
				}
				if(procCode != null && hasValue(procCode.getShortDescription())){
					code.setShortDescription(procCode.getShortDescription());
				}
				code.setSource(ClaimConstants.CLAIM_SOURCE.OHI.toString().toString());
				//adding procedure date
				if(claim.getIcdProc17Dt () !=  null){
					pCode.setProcedureDate(XMLUtil.getXMLDate(claim.getIcdProc17Dt()));					
				}
			}
			if(hasValue(claim.getIcdProc18Cd())){
				Procedure pCode = new Procedure();
				procedures.getProcedures().add(pCode);
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				pCode.getCodes().add(code);					
				code.setValue(claim.getIcdProc18Cd().trim());
				code.setIndex(new BigInteger("18"));
				code.setType("HIPAA");
				if(codeSubStype != null){
					code.setSubType(codeSubStype);
				}
				CodeSet procCode = codeDecodeUtil.getDecodedCodeProcedure(Constants.CODE.PROCEDURE,claim.getIcdProc18Cd().trim(),serviceStartDate,ClaimConstants.CLAIM_SOURCE.OHI);
				if(procCode != null && hasValue(procCode.getDescription())){
					code.setDescription(procCode.getDescription());
				}
				if(procCode != null && hasValue(procCode.getShortDescription())){
					code.setShortDescription(procCode.getShortDescription());
				}
				code.setSource(ClaimConstants.CLAIM_SOURCE.OHI.toString().toString());
				//adding procedure date
				if(claim.getIcdProc18Dt() !=  null){
					pCode.setProcedureDate(XMLUtil.getXMLDate(claim.getIcdProc18Dt()));						
				}
			}
			if(hasValue(claim.getIcdProc19Cd())){
				Procedure pCode = new Procedure();
				procedures.getProcedures().add(pCode);
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				pCode.getCodes().add(code);						
				code.setValue(claim.getIcdProc19Cd().trim());
				code.setIndex(new BigInteger("19"));
				code.setType("HIPAA");
				if(codeSubStype != null){
					code.setSubType(codeSubStype);
				}
				CodeSet procCode = codeDecodeUtil.getDecodedCodeProcedure(Constants.CODE.PROCEDURE,claim.getIcdProc19Cd().trim(),serviceStartDate,ClaimConstants.CLAIM_SOURCE.OHI);
				if(procCode != null && hasValue(procCode.getDescription())){
					code.setDescription(procCode.getDescription());
				}
				if(procCode != null && hasValue(procCode.getShortDescription())){
					code.setShortDescription(procCode.getShortDescription());
				}
				code.setSource(ClaimConstants.CLAIM_SOURCE.OHI.toString().toString());
				//adding procedure date
				if(claim.getIcdProc19Dt () !=  null){
					pCode.setProcedureDate(XMLUtil.getXMLDate(claim.getIcdProc19Dt()));						
				}
			}
			if(hasValue(claim.getIcdProc20Cd())){
				Procedure pCode = new Procedure();
				procedures.getProcedures().add(pCode);
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				pCode.getCodes().add(code);						
				code.setValue(claim.getIcdProc20Cd().trim());
				code.setIndex(new BigInteger("20"));
				code.setType("HIPAA");
				if(codeSubStype != null){
					code.setSubType(codeSubStype);
				}
				CodeSet procCode = codeDecodeUtil.getDecodedCodeProcedure(Constants.CODE.PROCEDURE,claim.getIcdProc20Cd().trim(),serviceStartDate,ClaimConstants.CLAIM_SOURCE.OHI);
				if(procCode != null && hasValue(procCode.getDescription())){
					code.setDescription(procCode.getDescription());
				}
				if(procCode != null && hasValue(procCode.getShortDescription())){
					code.setShortDescription(procCode.getShortDescription());
				}
				code.setSource(ClaimConstants.CLAIM_SOURCE.OHI.toString().toString());
				//adding procedure date
				if(claim.getIcdProc20Dt () !=  null){
					pCode.setProcedureDate(XMLUtil.getXMLDate(claim.getIcdProc20Dt()));						
				}
			}
			if(hasValue(claim.getIcdProc21Cd())){
				Procedure pCode = new Procedure();
				procedures.getProcedures().add(pCode);
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				pCode.getCodes().add(code);		
				code.setValue(claim.getIcdProc21Cd().trim());
				code.setIndex(new BigInteger("21"));
				code.setType("HIPAA");
				if(codeSubStype != null){
					code.setSubType(codeSubStype);
				}
				CodeSet procCode = codeDecodeUtil.getDecodedCodeProcedure(Constants.CODE.PROCEDURE,claim.getIcdProc21Cd().trim(),serviceStartDate,ClaimConstants.CLAIM_SOURCE.OHI);
				if(procCode != null && hasValue(procCode.getDescription())){
					code.setDescription(procCode.getDescription());
				}
				if(procCode != null && hasValue(procCode.getShortDescription())){
					code.setShortDescription(procCode.getShortDescription());
				}
				code.setSource(ClaimConstants.CLAIM_SOURCE.OHI.toString().toString());
				//adding procedure date
				if(claim.getIcdProc21Dt () !=  null){
					pCode.setProcedureDate(XMLUtil.getXMLDate(claim.getIcdProc20Dt()));						
				}
			}
			if(hasValue(claim.getIcdProc22Cd())){
				Procedure pCode = new Procedure();
				procedures.getProcedures().add(pCode);
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				pCode.getCodes().add(code);				
				code.setValue(claim.getIcdProc22Cd().trim());
				code.setIndex(new BigInteger("22"));
				code.setType("HIPAA");
				if(codeSubStype != null){
					code.setSubType(codeSubStype);
				}
				CodeSet procCode = codeDecodeUtil.getDecodedCodeProcedure(Constants.CODE.PROCEDURE,claim.getIcdProc22Cd().trim(),serviceStartDate,ClaimConstants.CLAIM_SOURCE.OHI);
				if(procCode != null && hasValue(procCode.getDescription())){
					code.setDescription(procCode.getDescription());
				}
				if(procCode != null && hasValue(procCode.getShortDescription())){
					code.setShortDescription(procCode.getShortDescription());
				}
				code.setSource(ClaimConstants.CLAIM_SOURCE.OHI.toString().toString());
				//adding procedure date
				if(claim.getIcdProc22Dt () !=  null){
					pCode.setProcedureDate(XMLUtil.getXMLDate(claim.getIcdProc22Dt()));							
				}
			}
			if(hasValue(claim.getIcdProc23Cd())){
				Procedure pCode = new Procedure();
				procedures.getProcedures().add(pCode);
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				pCode.getCodes().add(code);					
				code.setValue(claim.getIcdProc23Cd().trim());
				code.setIndex(new BigInteger("23"));
				code.setType("HIPAA");
				if(codeSubStype != null){
					code.setSubType(codeSubStype);
				}
				CodeSet procCode = codeDecodeUtil.getDecodedCodeProcedure(Constants.CODE.PROCEDURE,claim.getIcdProc23Cd().trim(),serviceStartDate,ClaimConstants.CLAIM_SOURCE.OHI);
				if(procCode != null && hasValue(procCode.getDescription())){
					code.setDescription(procCode.getDescription());
				}
				if(procCode != null && hasValue(procCode.getShortDescription())){
					code.setShortDescription(procCode.getShortDescription());
				}
				code.setSource(ClaimConstants.CLAIM_SOURCE.OHI.toString().toString());
				//adding procedure date
				if(claim.getIcdProc23Dt () !=  null){
					pCode.setProcedureDate(XMLUtil.getXMLDate(claim.getIcdProc23Dt()));	
				}
			}
			if(hasValue(claim.getIcdProc24Cd())){
				Procedure pCode = new Procedure();
				procedures.getProcedures().add(pCode);
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				pCode.getCodes().add(code);					
				code.setValue(claim.getIcdProc24Cd().trim());
				code.setIndex(new BigInteger("24"));
				code.setType("HIPAA");
				if(codeSubStype != null){
					code.setSubType(codeSubStype);
				}
				CodeSet procCode = codeDecodeUtil.getDecodedCodeProcedure(Constants.CODE.PROCEDURE,claim.getIcdProc24Cd().trim(),serviceStartDate,ClaimConstants.CLAIM_SOURCE.OHI);
				if(procCode != null && hasValue(procCode.getDescription())){
					code.setDescription(procCode.getDescription());
				}
				if(procCode != null && hasValue(procCode.getShortDescription())){
					code.setShortDescription(procCode.getShortDescription());
				}
				code.setSource(ClaimConstants.CLAIM_SOURCE.OHI.toString().toString());
				//adding procedure date
				if(claim.getIcdProc24Dt () !=  null){
					pCode.setProcedureDate(XMLUtil.getXMLDate(claim.getIcdProc24Dt()));						
				}
			}
			if(hasValue(claim.getIcdProc25Cd())){
				Procedure pCode = new Procedure();
				procedures.getProcedures().add(pCode);
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				pCode.getCodes().add(code);	
				code.setValue(claim.getIcdProc25Cd().trim());
				code.setIndex(new BigInteger("25"));
				code.setType("HIPAA");
				if(codeSubStype != null){
					code.setSubType(codeSubStype);
				}
				CodeSet procCode = codeDecodeUtil.getDecodedCodeProcedure(Constants.CODE.PROCEDURE,claim.getIcdProc25Cd().trim(),serviceStartDate,ClaimConstants.CLAIM_SOURCE.OHI);
				if(procCode != null && hasValue(procCode.getDescription())){
					code.setDescription(procCode.getDescription());
				}
				if(procCode != null && hasValue(procCode.getShortDescription())){
					code.setShortDescription(procCode.getShortDescription());
				}
				code.setSource(ClaimConstants.CLAIM_SOURCE.OHI.toString().toString());
				//adding procedure date
				if(claim.getIcdProc25Dt () !=  null){
					pCode.setProcedureDate(XMLUtil.getXMLDate(claim.getIcdProc25Dt()));	
				}
			}
			if(hasValue(claim.getIcdProc26Cd())){
				Procedure pCode = new Procedure();
				procedures.getProcedures().add(pCode);
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				pCode.getCodes().add(code);					
				code.setValue(claim.getIcdProc26Cd().trim());
				code.setIndex(new BigInteger("26"));
				code.setType("HIPAA");
				if(codeSubStype != null){
					code.setSubType(codeSubStype);
				}
				CodeSet procCode = codeDecodeUtil.getDecodedCodeProcedure(Constants.CODE.PROCEDURE,claim.getIcdProc26Cd().trim(),serviceStartDate,ClaimConstants.CLAIM_SOURCE.OHI);
				if(procCode != null && hasValue(procCode.getDescription())){
					code.setDescription(procCode.getDescription());
				}
				if(procCode != null && hasValue(procCode.getShortDescription())){
					code.setShortDescription(procCode.getShortDescription());
				}
				code.setSource(ClaimConstants.CLAIM_SOURCE.OHI.toString().toString());
				//adding procedure date
				if(claim.getIcdProc26Dt () !=  null){
					pCode.setProcedureDate(XMLUtil.getXMLDate(claim.getIcdProc26Dt()));						
				}
			}
		}
		
	}

	/**
	 * @param claim
	 * @param mBEHClaim
	 */
	private void addClaimDiagnosis(StgCl claim, MedicalBehClaimType mBEHClaim,CodeDecodeUtil codeDecodeUtil) {

		String codeSubStype = null;
		
		if(hasValue(claim.getDiag1Cd()) || hasValue(claim.getDiag2Cd()) ||
				hasValue(claim.getDiag3Cd()) || hasValue(claim.getDiag4Cd()) ||
				hasValue(claim.getDiag5Cd()) || hasValue(claim.getDiag6Cd()) ||
				hasValue(claim.getDiag7Cd()) || hasValue(claim.getDiag8Cd()) ||
				hasValue(claim.getDiag9Cd()) || hasValue(claim.getDiag10Cd()) ||
				hasValue(claim.getDiag11Cd()) || hasValue(claim.getDiag12Cd()) ||
				hasValue(claim.getDiag13Cd()) || hasValue(claim.getDiag14Cd()) ||
				hasValue(claim.getDiag15Cd()) || hasValue(claim.getDiag16Cd()) ||
				hasValue(claim.getDiag17Cd()) || hasValue(claim.getDiag18Cd()) ||
				hasValue(claim.getDiag19Cd()) || hasValue(claim.getDiag20Cd()) ||
				hasValue(claim.getDiag21Cd()) || hasValue(claim.getDiag22Cd()) ||
				hasValue(claim.getDiag23Cd()) || hasValue(claim.getDiag24Cd()) ||
				hasValue(claim.getDiag25Cd()) || hasValue(claim.getDiag26Cd())){
		
			Diagnoses diagnoses = new Diagnoses();
			mBEHClaim.setDiagnoses(diagnoses);
			if(claim.getClIcdRevisNbr() != null){
				codeSubStype = "ICD-" + claim.getClIcdRevisNbr();
			}
			if(hasValue(claim.getDiag1Cd())){
				DiagnosisType diagnosisType = new DiagnosisType(); 
				diagnoses.getDiagnosises().add(diagnosisType);
				diagnosisType.setIndex(new BigInteger("1"));
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				diagnosisType.getCodes().add(code);
				code.setValue(claim.getDiag1Cd().trim());
				code.setIndex(new BigInteger("0"));
				code.setType(ClaimConstants.HIPAA);
				if(codeSubStype != null){
					code.setSubType(codeSubStype);
				}
				String description = codeDecodeUtil.decodeDiagnosis(claim.getDiag1Cd().trim(),ClaimConstants.CLAIM_SOURCE.OHI);
				if(hasValue(description)){
					code.setDescription(description);
				}
				
			}
			if(hasValue(claim.getDiag2Cd())){
				DiagnosisType diagnosisType = new DiagnosisType();
				diagnoses.getDiagnosises().add(diagnosisType);
				diagnosisType.setIndex(new BigInteger("2"));
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				diagnosisType.getCodes().add(code);
				code.setValue(claim.getDiag2Cd().trim());
				code.setIndex(new BigInteger("0"));
				code.setType(ClaimConstants.HIPAA);
				if(codeSubStype != null){
					code.setSubType(codeSubStype);
				}
				String description = codeDecodeUtil.decodeDiagnosis(claim.getDiag2Cd().trim(),ClaimConstants.CLAIM_SOURCE.OHI);
				if(hasValue(description)){
					code.setDescription(description);
				}
			}
			if(hasValue(claim.getDiag3Cd())){
				DiagnosisType diagnosisType = new DiagnosisType();
				diagnoses.getDiagnosises().add(diagnosisType);
				diagnosisType.setIndex(new BigInteger("3"));
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				diagnosisType.getCodes().add(code);
				code.setValue(claim.getDiag3Cd().trim());
				code.setIndex(new BigInteger("0"));
				code.setType(ClaimConstants.HIPAA);
				if(codeSubStype != null){
					code.setSubType(codeSubStype);
				}
				String description = codeDecodeUtil.decodeDiagnosis(claim.getDiag3Cd().trim(),ClaimConstants.CLAIM_SOURCE.OHI);
				if(hasValue(description)){
					code.setDescription(description);
				}
			}
			if(hasValue(claim.getDiag4Cd())){
				DiagnosisType diagnosisType = new DiagnosisType();
				diagnoses.getDiagnosises().add(diagnosisType);
				diagnosisType.setIndex(new BigInteger("4"));
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				diagnosisType.getCodes().add(code);
				code.setValue(claim.getDiag4Cd().trim());
				code.setIndex(new BigInteger("0"));
				code.setType(ClaimConstants.HIPAA);
				if(codeSubStype != null){
					code.setSubType(codeSubStype);
				}
				String description = codeDecodeUtil.decodeDiagnosis(claim.getDiag4Cd().trim(),ClaimConstants.CLAIM_SOURCE.OHI);
				if(hasValue(description)){
					code.setDescription(description);
				}
			}
			if(hasValue(claim.getDiag5Cd())){
				DiagnosisType diagnosisType = new DiagnosisType();
				diagnoses.getDiagnosises().add(diagnosisType);
				diagnosisType.setIndex(new BigInteger("5"));
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				diagnosisType.getCodes().add(code);
				code.setValue(claim.getDiag5Cd().trim());
				code.setIndex(new BigInteger("0"));
				code.setType(ClaimConstants.HIPAA);
				if(codeSubStype != null){
					code.setSubType(codeSubStype);
				}
				String description = codeDecodeUtil.decodeDiagnosis(claim.getDiag5Cd().trim(),ClaimConstants.CLAIM_SOURCE.OHI);
				if(hasValue(description)){
					code.setDescription(description);
				}
			}
			if(hasValue(claim.getDiag6Cd())){
				DiagnosisType diagnosisType = new DiagnosisType();
				diagnoses.getDiagnosises().add(diagnosisType);
				diagnosisType.setIndex(new BigInteger("6"));
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				diagnosisType.getCodes().add(code);
				code.setValue(claim.getDiag6Cd().trim());
				code.setIndex(new BigInteger("0"));
				code.setType(ClaimConstants.HIPAA);
				if(codeSubStype != null){
					code.setSubType(codeSubStype);
				}
				String description = codeDecodeUtil.decodeDiagnosis(claim.getDiag6Cd().trim(),ClaimConstants.CLAIM_SOURCE.OHI);
				if(hasValue(description)){
					code.setDescription(description);
				}
			}
			if(hasValue(claim.getDiag7Cd())){
				DiagnosisType diagnosisType = new DiagnosisType();
				diagnoses.getDiagnosises().add(diagnosisType);
				diagnosisType.setIndex(new BigInteger("7"));
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				diagnosisType.getCodes().add(code);
				code.setValue(claim.getDiag7Cd().trim());
				code.setIndex(new BigInteger("0"));
				code.setType(ClaimConstants.HIPAA);
				if(codeSubStype != null){
					code.setSubType(codeSubStype);
				}
				String description = codeDecodeUtil.decodeDiagnosis(claim.getDiag7Cd().trim(),ClaimConstants.CLAIM_SOURCE.OHI);
				if(hasValue(description)){
					code.setDescription(description);
				}
			}
			if(hasValue(claim.getDiag8Cd())){
				DiagnosisType diagnosisType = new DiagnosisType();
				diagnoses.getDiagnosises().add(diagnosisType);
				diagnosisType.setIndex(new BigInteger("8"));
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				diagnosisType.getCodes().add(code);
				code.setValue(claim.getDiag8Cd().trim());
				code.setIndex(new BigInteger("0"));
				code.setType(ClaimConstants.HIPAA);
				if(codeSubStype != null){
					code.setSubType(codeSubStype);
				}
				String description = codeDecodeUtil.decodeDiagnosis(claim.getDiag8Cd().trim(),ClaimConstants.CLAIM_SOURCE.OHI);
				if(hasValue(description)){
					code.setDescription(description);
				}
			}
			if(hasValue(claim.getDiag9Cd())){
				DiagnosisType diagnosisType = new DiagnosisType();
				diagnoses.getDiagnosises().add(diagnosisType);
				diagnosisType.setIndex(new BigInteger("9"));
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				diagnosisType.getCodes().add(code);
				code.setValue(claim.getDiag9Cd().trim());
				code.setIndex(new BigInteger("0"));
				code.setType(ClaimConstants.HIPAA);
				if(codeSubStype != null){
					code.setSubType(codeSubStype);
				}
				String description = codeDecodeUtil.decodeDiagnosis(claim.getDiag9Cd().trim(),ClaimConstants.CLAIM_SOURCE.OHI);
				if(hasValue(description)){
					code.setDescription(description);
				}
			}
			if(hasValue(claim.getDiag10Cd())){
				DiagnosisType diagnosisType = new DiagnosisType();
				diagnoses.getDiagnosises().add(diagnosisType);
				diagnosisType.setIndex(new BigInteger("10"));
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				diagnosisType.getCodes().add(code);
				code.setValue(claim.getDiag10Cd().trim());
				code.setIndex(new BigInteger("0"));
				code.setType(ClaimConstants.HIPAA);
				if(codeSubStype != null){
					code.setSubType(codeSubStype);
				}
				String description = codeDecodeUtil.decodeDiagnosis(claim.getDiag10Cd().trim(),ClaimConstants.CLAIM_SOURCE.OHI);
				if(hasValue(description)){
					code.setDescription(description);
				}
			}
			if(hasValue(claim.getDiag11Cd())){
				DiagnosisType diagnosisType = new DiagnosisType();
				diagnoses.getDiagnosises().add(diagnosisType);
				diagnosisType.setIndex(new BigInteger("11"));
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				diagnosisType.getCodes().add(code);
				code.setValue(claim.getDiag11Cd().trim());
				code.setIndex(new BigInteger("0"));
				code.setType(ClaimConstants.HIPAA);
				if(codeSubStype != null){
					code.setSubType(codeSubStype);
				}
				String description = codeDecodeUtil.decodeDiagnosis(claim.getDiag11Cd().trim(),ClaimConstants.CLAIM_SOURCE.OHI);
				if(hasValue(description)){
					code.setDescription(description);
				}
			}
			if(hasValue(claim.getDiag12Cd())){
				DiagnosisType diagnosisType = new DiagnosisType();
				diagnoses.getDiagnosises().add(diagnosisType);
				diagnosisType.setIndex(new BigInteger("12"));
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				diagnosisType.getCodes().add(code);
				code.setValue(claim.getDiag12Cd().trim());
				code.setIndex(new BigInteger("0"));
				code.setType(ClaimConstants.HIPAA);
				if(codeSubStype != null){
					code.setSubType(codeSubStype);
				}
				String description = codeDecodeUtil.decodeDiagnosis(claim.getDiag12Cd().trim(),ClaimConstants.CLAIM_SOURCE.OHI);
				if(hasValue(description)){
					code.setDescription(description);
				}
			}
			if(hasValue(claim.getDiag13Cd())){
				DiagnosisType diagnosisType = new DiagnosisType();
				diagnoses.getDiagnosises().add(diagnosisType);
				diagnosisType.setIndex(new BigInteger("13"));
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				diagnosisType.getCodes().add(code);
				code.setValue(claim.getDiag13Cd().trim());
				code.setIndex(new BigInteger("0"));
				code.setType(ClaimConstants.HIPAA);
				if(codeSubStype != null){
					code.setSubType(codeSubStype);
				}
				String description = codeDecodeUtil.decodeDiagnosis(claim.getDiag13Cd().trim(),ClaimConstants.CLAIM_SOURCE.OHI);
				if(hasValue(description)){
					code.setDescription(description);
				}
			}
			if(hasValue(claim.getDiag14Cd())){
				DiagnosisType diagnosisType = new DiagnosisType();
				diagnoses.getDiagnosises().add(diagnosisType);
				diagnosisType.setIndex(new BigInteger("14"));
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				diagnosisType.getCodes().add(code);
				code.setValue(claim.getDiag14Cd().trim());
				code.setIndex(new BigInteger("0"));
				code.setType(ClaimConstants.HIPAA);
				if(codeSubStype != null){
					code.setSubType(codeSubStype);
				}
				String description = codeDecodeUtil.decodeDiagnosis(claim.getDiag14Cd().trim(),ClaimConstants.CLAIM_SOURCE.OHI);
				if(hasValue(description)){
					code.setDescription(description);
				}
			}
			if(hasValue(claim.getDiag15Cd())){
				DiagnosisType diagnosisType = new DiagnosisType();
				diagnoses.getDiagnosises().add(diagnosisType);
				diagnosisType.setIndex(new BigInteger("15"));
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				diagnosisType.getCodes().add(code);
				code.setValue(claim.getDiag15Cd().trim());
				code.setIndex(new BigInteger("0"));
				code.setType(ClaimConstants.HIPAA);
				if(codeSubStype != null){
					code.setSubType(codeSubStype);
				}
				String description = codeDecodeUtil.decodeDiagnosis(claim.getDiag15Cd().trim(),ClaimConstants.CLAIM_SOURCE.OHI);
				if(hasValue(description)){
					code.setDescription(description);
				}
			}
			if(hasValue(claim.getDiag16Cd())){
				DiagnosisType diagnosisType = new DiagnosisType();
				diagnoses.getDiagnosises().add(diagnosisType);
				diagnosisType.setIndex(new BigInteger("16"));
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				diagnosisType.getCodes().add(code);
				code.setValue(claim.getDiag16Cd().trim());
				code.setIndex(new BigInteger("0"));
				code.setType(ClaimConstants.HIPAA);
				if(codeSubStype != null){
					code.setSubType(codeSubStype);
				}
				String description = codeDecodeUtil.decodeDiagnosis(claim.getDiag16Cd().trim(),ClaimConstants.CLAIM_SOURCE.OHI);
				if(hasValue(description)){
					code.setDescription(description);
				}
			}
			if(hasValue(claim.getDiag17Cd())){
				DiagnosisType diagnosisType = new DiagnosisType();
				diagnoses.getDiagnosises().add(diagnosisType);
				diagnosisType.setIndex(new BigInteger("17"));
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				diagnosisType.getCodes().add(code);
				code.setValue(claim.getDiag17Cd().trim());
				code.setIndex(new BigInteger("0"));
				code.setType(ClaimConstants.HIPAA);
				if(codeSubStype != null){
					code.setSubType(codeSubStype);
				}
				String description = codeDecodeUtil.decodeDiagnosis(claim.getDiag17Cd().trim(),ClaimConstants.CLAIM_SOURCE.OHI);
				if(hasValue(description)){
					code.setDescription(description);
				}
			}
			if(hasValue(claim.getDiag18Cd())){
				DiagnosisType diagnosisType = new DiagnosisType();
				diagnoses.getDiagnosises().add(diagnosisType);
				diagnosisType.setIndex(new BigInteger("18"));
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				diagnosisType.getCodes().add(code);
				code.setValue(claim.getDiag18Cd().trim());
				code.setIndex(new BigInteger("0"));
				code.setType(ClaimConstants.HIPAA);
				if(codeSubStype != null){
					code.setSubType(codeSubStype);
				}
				String description = codeDecodeUtil.decodeDiagnosis(claim.getDiag18Cd().trim(),ClaimConstants.CLAIM_SOURCE.OHI);
				if(hasValue(description)){
					code.setDescription(description);
				}
			}
			if(hasValue(claim.getDiag19Cd())){
				DiagnosisType diagnosisType = new DiagnosisType();
				diagnoses.getDiagnosises().add(diagnosisType);
				diagnosisType.setIndex(new BigInteger("19"));
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				diagnosisType.getCodes().add(code);
				code.setValue(claim.getDiag19Cd().trim());
				code.setIndex(new BigInteger("0"));
				code.setType(ClaimConstants.HIPAA);
				if(codeSubStype != null){
					code.setSubType(codeSubStype);
				}
				String description = codeDecodeUtil.decodeDiagnosis(claim.getDiag19Cd().trim(),ClaimConstants.CLAIM_SOURCE.OHI);
				if(hasValue(description)){
					code.setDescription(description);
				}
			}
			if(hasValue(claim.getDiag20Cd())){
				DiagnosisType diagnosisType = new DiagnosisType();
				diagnoses.getDiagnosises().add(diagnosisType);
				diagnosisType.setIndex(new BigInteger("20"));			
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				diagnosisType.getCodes().add(code);
				code.setValue(claim.getDiag20Cd().trim());
				code.setIndex(new BigInteger("0"));
				code.setType(ClaimConstants.HIPAA);
				if(codeSubStype != null){
					code.setSubType(codeSubStype);
				}
				String description = codeDecodeUtil.decodeDiagnosis(claim.getDiag20Cd().trim(),ClaimConstants.CLAIM_SOURCE.OHI);
				if(hasValue(description)){
					code.setDescription(description);
				}
			}
			if(hasValue(claim.getDiag21Cd())){
				DiagnosisType diagnosisType = new DiagnosisType();
				diagnoses.getDiagnosises().add(diagnosisType);
				diagnosisType.setIndex(new BigInteger("21"));			
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				diagnosisType.getCodes().add(code);
				code.setValue(claim.getDiag21Cd().trim());
				code.setIndex(new BigInteger("0"));
				code.setType(ClaimConstants.HIPAA);
				if(codeSubStype != null){
					code.setSubType(codeSubStype);
				}
				String description = codeDecodeUtil.decodeDiagnosis(claim.getDiag21Cd().trim(),ClaimConstants.CLAIM_SOURCE.OHI);
				if(hasValue(description)){
					code.setDescription(description);
				}
			}
			if(hasValue(claim.getDiag22Cd())){
				DiagnosisType diagnosisType = new DiagnosisType();
				diagnoses.getDiagnosises().add(diagnosisType);
				diagnosisType.setIndex(new BigInteger("22"));			
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				diagnosisType.getCodes().add(code);
				code.setValue(claim.getDiag22Cd().trim());
				code.setIndex(new BigInteger("0"));
				code.setType(ClaimConstants.HIPAA);
				if(codeSubStype != null){
					code.setSubType(codeSubStype);
				}
				String description = codeDecodeUtil.decodeDiagnosis(claim.getDiag22Cd().trim(),ClaimConstants.CLAIM_SOURCE.OHI);
				if(hasValue(description)){
					code.setDescription(description);
				}
			}
			if(hasValue(claim.getDiag23Cd())){
				DiagnosisType diagnosisType = new DiagnosisType();
				diagnoses.getDiagnosises().add(diagnosisType);
				diagnosisType.setIndex(new BigInteger("23"));			
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				diagnosisType.getCodes().add(code);
				code.setValue(claim.getDiag23Cd().trim());
				code.setIndex(new BigInteger("0"));
				code.setType(ClaimConstants.HIPAA);
				if(codeSubStype != null){
					code.setSubType(codeSubStype);
				}
				String description = codeDecodeUtil.decodeDiagnosis(claim.getDiag23Cd().trim(),ClaimConstants.CLAIM_SOURCE.OHI);
				if(hasValue(description)){
					code.setDescription(description);
				}
			}
			if(hasValue(claim.getDiag24Cd())){
				DiagnosisType diagnosisType = new DiagnosisType();
				diagnoses.getDiagnosises().add(diagnosisType);
				diagnosisType.setIndex(new BigInteger("24"));				
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				diagnosisType.getCodes().add(code);
				code.setValue(claim.getDiag24Cd().trim());
				code.setIndex(new BigInteger("0"));
				code.setType(ClaimConstants.HIPAA);
				if(codeSubStype != null){
					code.setSubType(codeSubStype);
				}
				String description = codeDecodeUtil.decodeDiagnosis(claim.getDiag24Cd().trim(),ClaimConstants.CLAIM_SOURCE.OHI);
				if(hasValue(description)){
					code.setDescription(description);
				}
			}
			if(hasValue(claim.getDiag25Cd())){
				DiagnosisType diagnosisType = new DiagnosisType();
				diagnoses.getDiagnosises().add(diagnosisType);
				diagnosisType.setIndex(new BigInteger("25"));				
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				diagnosisType.getCodes().add(code);
				code.setValue(claim.getDiag25Cd().trim());
				code.setIndex(new BigInteger("0"));
				code.setType(ClaimConstants.HIPAA);
				if(codeSubStype != null){
					code.setSubType(codeSubStype);
				}
				String description = codeDecodeUtil.decodeDiagnosis(claim.getDiag25Cd().trim(),ClaimConstants.CLAIM_SOURCE.OHI);
				if(hasValue(description)){
					code.setDescription(description);
				}
			}
			if(hasValue(claim.getDiag26Cd())){
				DiagnosisType diagnosisType = new DiagnosisType();
				diagnoses.getDiagnosises().add(diagnosisType);
				diagnosisType.setIndex(new BigInteger("26"));				
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				diagnosisType.getCodes().add(code);
				code.setValue(claim.getDiag26Cd().trim());
				code.setIndex(new BigInteger("0"));
				code.setType(ClaimConstants.HIPAA);
				if(codeSubStype != null){
					code.setSubType(codeSubStype);
				}
				String description = codeDecodeUtil.decodeDiagnosis(claim.getDiag26Cd().trim(),ClaimConstants.CLAIM_SOURCE.OHI);
				if(hasValue(description)){
					code.setDescription(description);
				}
			}
		}
	}

	/**
	 * @param claim
	 * @param mBEHClaim
	 * Not used for now. Kept for future use.
	 */
	@SuppressWarnings("unused")
	private void addClaimAdmittingDiagnosis(StgCl claim, MedicalBehClaimType mBEHClaim) {

		if(hasValue(claim.getAdmitDiagCd())){
			Diagnoses diagnoses = mBEHClaim.getDiagnoses();
			if(diagnoses == null){
				diagnoses = new Diagnoses();
				mBEHClaim.setDiagnoses(diagnoses);
			}
			if(hasValue(claim.getAdmitDiagCd())){
				DiagnosisType diagnosisType = new DiagnosisType();
				diagnoses.getDiagnosises().add(diagnosisType);
				diagnosisType.setIndex(new BigInteger("1"));
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				diagnosisType.getCodes().add(code);
				code.setValue(claim.getAdmitDiagCd().trim());
				code.setIndex(new BigInteger("0"));
				code.setType(ClaimConstants.HPHC);

				diagnosisType.setType(org.hphc.schema.claim.v6.DiagnosisEnumType.ADMITTING);
			}
		}
	}

	/**
	 * @param claim
	 * @param mBEHClaim
	 * Not used for now.Kept for future use.
	 */
	@SuppressWarnings("unused")
	private void addClaimECodeDiagnosis(StgCl claim, MedicalBehClaimType mBEHClaim) {

		if(hasValue(claim.getEdiag1Cd()) || hasValue(claim.getEdiag2Cd()) ||
				hasValue(claim.getEdiag3Cd()) || hasValue(claim.getEdiag4Cd()) ||
				hasValue(claim.getEdiag5Cd()) || hasValue(claim.getEdiag6Cd()) ||
				hasValue(claim.getEdiag7Cd()) || hasValue(claim.getEdiag8Cd()) ||
				hasValue(claim.getEdiag9Cd()) || hasValue(claim.getEdiag10Cd())){
			Diagnoses diagnoses = mBEHClaim.getDiagnoses();
			if(diagnoses == null){
				diagnoses = new Diagnoses();
				mBEHClaim.setDiagnoses(diagnoses);				
			}
			if(hasValue(claim.getEdiag1Cd())){
				DiagnosisType diagnosisType = new DiagnosisType();
				diagnoses.getDiagnosises().add(diagnosisType);
				diagnosisType.setIndex(new BigInteger("1"));
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				diagnosisType.getCodes().add(code);
				code.setValue(claim.getEdiag1Cd().trim());
				code.setIndex(new BigInteger("0"));
				code.setType(ClaimConstants.HPHC);
				diagnosisType.setType(org.hphc.schema.claim.v6.DiagnosisEnumType.E_CODE);
			}
			if(hasValue(claim.getEdiag2Cd())){
				DiagnosisType diagnosisType = new DiagnosisType();
				diagnoses.getDiagnosises().add(diagnosisType);
				diagnosisType.setIndex(new BigInteger("2"));
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				diagnosisType.getCodes().add(code);
				code.setValue(claim.getEdiag2Cd().trim());
				code.setIndex(new BigInteger("0"));
				code.setType(ClaimConstants.HPHC);
				diagnosisType.setType(org.hphc.schema.claim.v6.DiagnosisEnumType.E_CODE);
			}
			if(hasValue(claim.getEdiag3Cd())){
				DiagnosisType diagnosisType = new DiagnosisType();
				diagnoses.getDiagnosises().add(diagnosisType);
				diagnosisType.setIndex(new BigInteger("3"));
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				diagnosisType.getCodes().add(code);
				code.setValue(claim.getEdiag3Cd().trim());
				code.setIndex(new BigInteger("0"));
				code.setType(ClaimConstants.HPHC);
				diagnosisType.setType(org.hphc.schema.claim.v6.DiagnosisEnumType.E_CODE);
			}
			if(hasValue(claim.getEdiag4Cd())){
				DiagnosisType diagnosisType = new DiagnosisType();
				diagnoses.getDiagnosises().add(diagnosisType);
				diagnosisType.setIndex(new BigInteger("4"));
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				diagnosisType.getCodes().add(code);
				code.setValue(claim.getEdiag4Cd().trim());
				code.setIndex(new BigInteger("0"));
				code.setType(ClaimConstants.HPHC);
				diagnosisType.setType(org.hphc.schema.claim.v6.DiagnosisEnumType.E_CODE);
			}
			if(hasValue(claim.getEdiag5Cd())){
				DiagnosisType diagnosisType = new DiagnosisType();
				diagnoses.getDiagnosises().add(diagnosisType);
				diagnosisType.setIndex(new BigInteger("5"));
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				diagnosisType.getCodes().add(code);
				code.setValue(claim.getEdiag5Cd().trim());
				code.setIndex(new BigInteger("0"));
				code.setType(ClaimConstants.HPHC);
				diagnosisType.setType(org.hphc.schema.claim.v6.DiagnosisEnumType.E_CODE);
			}
			if(hasValue(claim.getEdiag6Cd())){
				DiagnosisType diagnosisType = new DiagnosisType();
				diagnoses.getDiagnosises().add(diagnosisType);
				diagnosisType.setIndex(new BigInteger("6"));
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				diagnosisType.getCodes().add(code);
				code.setValue(claim.getEdiag6Cd().trim());
				code.setIndex(new BigInteger("0"));
				code.setType(ClaimConstants.HPHC);
				diagnosisType.setType(org.hphc.schema.claim.v6.DiagnosisEnumType.E_CODE);
			}
			if(hasValue(claim.getEdiag7Cd())){
				DiagnosisType diagnosisType = new DiagnosisType();
				diagnoses.getDiagnosises().add(diagnosisType);
				diagnosisType.setIndex(new BigInteger("7"));
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				diagnosisType.getCodes().add(code);
				code.setValue(claim.getEdiag7Cd().trim());
				code.setIndex(new BigInteger("0"));
				code.setType(ClaimConstants.HPHC);
				diagnosisType.setType(org.hphc.schema.claim.v6.DiagnosisEnumType.E_CODE);
			}
			if(hasValue(claim.getEdiag8Cd())){
				DiagnosisType diagnosisType = new DiagnosisType();
				diagnoses.getDiagnosises().add(diagnosisType);
				diagnosisType.setIndex(new BigInteger("8"));
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				diagnosisType.getCodes().add(code);
				code.setValue(claim.getEdiag8Cd().trim());
				code.setIndex(new BigInteger("0"));
				code.setType(ClaimConstants.HPHC);
				diagnosisType.setType(org.hphc.schema.claim.v6.DiagnosisEnumType.E_CODE);
			}
			if(hasValue(claim.getEdiag9Cd())){
				DiagnosisType diagnosisType = new DiagnosisType();
				diagnoses.getDiagnosises().add(diagnosisType);
				diagnosisType.setIndex(new BigInteger("9"));
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				diagnosisType.getCodes().add(code);
				code.setValue(claim.getEdiag9Cd().trim());
				code.setIndex(new BigInteger("0"));
				code.setType(ClaimConstants.HPHC);
				diagnosisType.setType(org.hphc.schema.claim.v6.DiagnosisEnumType.E_CODE);
			}
			if(hasValue(claim.getEdiag10Cd())){
				DiagnosisType diagnosisType = new DiagnosisType();
				diagnoses.getDiagnosises().add(diagnosisType);
				diagnosisType.setIndex(new BigInteger("10"));
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				diagnosisType.getCodes().add(code);
				code.setValue(claim.getEdiag10Cd().trim());
				code.setIndex(new BigInteger("0"));
				code.setType(ClaimConstants.HPHC);
				diagnosisType.setType(org.hphc.schema.claim.v6.DiagnosisEnumType.E_CODE);
			}
		}
	}

	/**
	 * @param stgClSvc
	 * @param service
	 * Add Adjudication Benefit Codes
	 */
	private void addAdjudicationBenefitCodes(StgClSvc stgClSvc,
			MedicalBehServiceLineType service) {

		if(hasValue(stgClSvc.getAdjudBnftSpec1Cd()) 
				|| hasValue(stgClSvc.getAdjudBnftSpec2Cd()) 
				|| hasValue(stgClSvc.getAdjudBnftSpec3Cd())
				|| hasValue(stgClSvc.getAdjudBnftSpec4Cd())){
			AdjudicationBenefitCodes benefitCodes = new AdjudicationBenefitCodes();
			service.setAdjudicationBenefitCodes(benefitCodes);

			if(hasValue(stgClSvc.getAdjudBnftSpec1Cd())){
				MedicalBehServiceLineType.AdjudicationBenefitCodes.AdjudicationBenefitCode adjudicationBenefit = new MedicalBehServiceLineType.AdjudicationBenefitCodes.AdjudicationBenefitCode();
				benefitCodes.getAdjudicationBenefitCodes().add(adjudicationBenefit);
				adjudicationBenefit.setIndex(new BigInteger("1"));
				adjudicationBenefit.setCode(stgClSvc.getAdjudBnftSpec1Cd().trim());
				adjudicationBenefit.setValue(stgClSvc.getAdjudBnftSpec1Cd().trim());
				adjudicationBenefit.setCodeSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
			}
			if(hasValue(stgClSvc.getAdjudBnftSpec2Cd())){
				MedicalBehServiceLineType.AdjudicationBenefitCodes.AdjudicationBenefitCode adjudicationBenefit = new MedicalBehServiceLineType.AdjudicationBenefitCodes.AdjudicationBenefitCode();
				benefitCodes.getAdjudicationBenefitCodes().add(adjudicationBenefit);
				adjudicationBenefit.setIndex(new BigInteger("2"));
				adjudicationBenefit.setCode(stgClSvc.getAdjudBnftSpec2Cd().trim());
				adjudicationBenefit.setValue(stgClSvc.getAdjudBnftSpec2Cd().trim());
				adjudicationBenefit.setCodeSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
			}
			if(hasValue(stgClSvc.getAdjudBnftSpec3Cd())){
				MedicalBehServiceLineType.AdjudicationBenefitCodes.AdjudicationBenefitCode adjudicationBenefit = new MedicalBehServiceLineType.AdjudicationBenefitCodes.AdjudicationBenefitCode();
				benefitCodes.getAdjudicationBenefitCodes().add(adjudicationBenefit);
				adjudicationBenefit.setIndex(new BigInteger("3"));
				adjudicationBenefit.setCode(stgClSvc.getAdjudBnftSpec3Cd().trim());
				adjudicationBenefit.setValue(stgClSvc.getAdjudBnftSpec3Cd().trim());
				adjudicationBenefit.setCodeSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
			}
			if(hasValue(stgClSvc.getAdjudBnftSpec4Cd())){
				MedicalBehServiceLineType.AdjudicationBenefitCodes.AdjudicationBenefitCode adjudicationBenefit = new MedicalBehServiceLineType.AdjudicationBenefitCodes.AdjudicationBenefitCode();
				benefitCodes.getAdjudicationBenefitCodes().add(adjudicationBenefit);
				adjudicationBenefit.setIndex(new BigInteger("4"));
				adjudicationBenefit.setCode(stgClSvc.getAdjudBnftSpec4Cd().trim());
				adjudicationBenefit.setValue(stgClSvc.getAdjudBnftSpec4Cd().trim());
				adjudicationBenefit.setCodeSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
			}
		}
	}

	/**
	 * @param stgClSvc
	 * @param service
	 * Add Modifiers
	 */
	private void addModifiers(StgClSvc stgClSvc,
			MedicalBehServiceLineType service,CodeDecodeUtil codeDecodeUtil) {

		if((hasValue(stgClSvc.getPrcProcMod1Cd())) 
				|| hasValue(stgClSvc.getPrcProcMod2Cd())
				|| hasValue(stgClSvc.getSubmProcMod1Cd())
				|| hasValue(stgClSvc.getSubmProcMod2Cd())
				|| hasValue(stgClSvc.getSubmProcMod3Cd())
				|| hasValue(stgClSvc.getSubmProcMod4Cd())
				|| hasValue(stgClSvc.getAdjudProcMod1Cd())
				|| hasValue(stgClSvc.getAdjudProcMod2Cd())
				|| hasValue(stgClSvc.getAdjudProcMod3Cd())
				|| hasValue(stgClSvc.getAdjudProcMod4Cd())){

			Modifiers modifiers = new Modifiers();
			service.setModifiers(modifiers);

			if(hasValue(stgClSvc.getPrcProcMod1Cd())){
				Modifier modifier = new Modifier();
				modifiers.getModifiers().add(modifier);
				modifier.setCode(stgClSvc.getPrcProcMod1Cd().trim());
				modifier.setIndex(new BigInteger("1"));
				LOGGER.debug("Modifier : " + stgClSvc.getPrcProcMod1Cd().trim());
				modifier.setValue(codeDecodeUtil
						.decodeModifier(stgClSvc.getPrcProcMod1Cd().trim(),ClaimConstants.CLAIM_SOURCE.OHI));
				modifier.setLifeCycle(org.hphc.schema.claim.v6.LifeCycleEnumType.PRICED);
			}
			if(hasValue(stgClSvc.getPrcProcMod2Cd())){
				Modifier modifier = new Modifier();
				modifiers.getModifiers().add(modifier);
				modifier.setCode(stgClSvc.getPrcProcMod2Cd().trim());
				modifier.setIndex(new BigInteger("2"));
				LOGGER.debug("Modifier : " + stgClSvc.getPrcProcMod2Cd().trim());
				modifier.setValue(codeDecodeUtil
						.decodeModifier(stgClSvc.getPrcProcMod2Cd().trim(),ClaimConstants.CLAIM_SOURCE.OHI));
				modifier.setLifeCycle(org.hphc.schema.claim.v6.LifeCycleEnumType.PRICED);
			}
			if(hasValue(stgClSvc.getSubmProcMod1Cd())){
				Modifier modifier = new Modifier();
				modifiers.getModifiers().add(modifier);
				modifier.setCode(stgClSvc.getSubmProcMod1Cd().trim());
				modifier.setIndex(new BigInteger("1"));
				LOGGER.debug("Modifier : " + stgClSvc.getSubmProcMod1Cd().trim());
				modifier.setValue(codeDecodeUtil
						.decodeModifier(stgClSvc.getSubmProcMod1Cd().trim(),ClaimConstants.CLAIM_SOURCE.OHI));
				modifier.setLifeCycle(org.hphc.schema.claim.v6.LifeCycleEnumType.SUBMITTED);
			}
			if(hasValue(stgClSvc.getSubmProcMod2Cd())){
				Modifier modifier = new Modifier();
				modifiers.getModifiers().add(modifier);
				modifier.setCode(stgClSvc.getSubmProcMod2Cd().trim());
				modifier.setIndex(new BigInteger("2"));
				LOGGER.debug("Modifier : " + stgClSvc.getSubmProcMod2Cd().trim());
				modifier.setValue(codeDecodeUtil
						.decodeModifier(stgClSvc.getSubmProcMod2Cd().trim(),ClaimConstants.CLAIM_SOURCE.OHI));
				modifier.setLifeCycle(org.hphc.schema.claim.v6.LifeCycleEnumType.SUBMITTED);
			}
			if(hasValue(stgClSvc.getSubmProcMod3Cd())){
				Modifier modifier = new Modifier();
				modifiers.getModifiers().add(modifier);
				modifier.setCode(stgClSvc.getSubmProcMod3Cd().trim());
				modifier.setIndex(new BigInteger("3"));
				LOGGER.debug("Modifier : " + stgClSvc.getSubmProcMod3Cd().trim());
				modifier.setValue(codeDecodeUtil
						.decodeModifier(stgClSvc.getSubmProcMod3Cd().trim(),ClaimConstants.CLAIM_SOURCE.OHI));
				modifier.setLifeCycle(org.hphc.schema.claim.v6.LifeCycleEnumType.SUBMITTED);
			}
			if(hasValue(stgClSvc.getSubmProcMod4Cd())){
				Modifier modifier = new Modifier();
				modifiers.getModifiers().add(modifier);
				modifier.setCode(stgClSvc.getSubmProcMod4Cd().trim());
				modifier.setIndex(new BigInteger("4"));
				LOGGER.debug("Modifier : " + stgClSvc.getSubmProcMod4Cd().trim());
				modifier.setValue(codeDecodeUtil
						.decodeModifier(stgClSvc.getSubmProcMod4Cd().trim(),ClaimConstants.CLAIM_SOURCE.OHI));
				modifier.setLifeCycle(org.hphc.schema.claim.v6.LifeCycleEnumType.SUBMITTED);
			}
			if(hasValue(stgClSvc.getAdjudProcMod1Cd())){
				Modifier modifier = new Modifier();
				modifiers.getModifiers().add(modifier);
				modifier.setCode(stgClSvc.getAdjudProcMod1Cd().trim());
				modifier.setIndex(new BigInteger("1"));
				LOGGER.debug("Modifier : " + stgClSvc.getAdjudProcMod1Cd().trim());
				modifier.setValue(codeDecodeUtil
						.decodeModifier(stgClSvc.getAdjudProcMod1Cd().trim(),ClaimConstants.CLAIM_SOURCE.OHI));
				modifier.setLifeCycle(org.hphc.schema.claim.v6.LifeCycleEnumType.ADJUDICATED);
			}
			if(hasValue(stgClSvc.getAdjudProcMod2Cd())){
				Modifier modifier = new Modifier();
				modifiers.getModifiers().add(modifier);
				modifier.setCode(stgClSvc.getAdjudProcMod2Cd().trim());
				modifier.setIndex(new BigInteger("2"));
				LOGGER.debug("Modifier : " + stgClSvc.getAdjudProcMod2Cd().trim());
				modifier.setValue(codeDecodeUtil
						.decodeModifier(stgClSvc.getAdjudProcMod2Cd().trim(),ClaimConstants.CLAIM_SOURCE.OHI));
				modifier.setLifeCycle(org.hphc.schema.claim.v6.LifeCycleEnumType.ADJUDICATED);
			}
			if(hasValue(stgClSvc.getAdjudProcMod3Cd())){
				Modifier modifier = new Modifier();
				modifiers.getModifiers().add(modifier);
				modifier.setCode(stgClSvc.getAdjudProcMod3Cd().trim());
				modifier.setIndex(new BigInteger("3"));
				LOGGER.debug("Modifier : " + stgClSvc.getAdjudProcMod3Cd().trim());
				modifier.setValue(codeDecodeUtil
						.decodeModifier(stgClSvc.getAdjudProcMod3Cd().trim(),ClaimConstants.CLAIM_SOURCE.OHI));
				modifier.setLifeCycle(org.hphc.schema.claim.v6.LifeCycleEnumType.ADJUDICATED);
			}
			if(hasValue(stgClSvc.getAdjudProcMod4Cd())){
				Modifier modifier = new Modifier();
				modifiers.getModifiers().add(modifier);
				modifier.setCode(stgClSvc.getAdjudProcMod4Cd().trim());
				modifier.setIndex(new BigInteger("4"));
				LOGGER.debug("Modifier : " + stgClSvc.getAdjudProcMod4Cd().trim());
				modifier.setValue(codeDecodeUtil
						.decodeModifier(stgClSvc.getAdjudProcMod4Cd().trim(),ClaimConstants.CLAIM_SOURCE.OHI));
				modifier.setLifeCycle(org.hphc.schema.claim.v6.LifeCycleEnumType.ADJUDICATED);
			}
		}
	}

	/**
	 * @param stgClSvc
	 * @param service
	 * Add anesthesia minutes
	 */
	private void addMinutes(StgClSvc stgClSvc, MedicalBehServiceLineType service) {

		if(stgClSvc.getSubmAnsthMinCnt() != null 
				|| stgClSvc.getClmdAnsthMinCnt() != null){
			AnesthesiaMinutes minutes = new AnesthesiaMinutes();
			service.setMinutes(minutes);

			if(stgClSvc.getSubmAnsthMinCnt() != null){
				AnesthesiaMinutes.Minutes anesthesiaMinutes = new AnesthesiaMinutes.Minutes();
				minutes.getMinutes().add(anesthesiaMinutes);
				anesthesiaMinutes.setValue(stgClSvc.getSubmAnsthMinCnt().longValue());
				anesthesiaMinutes.setLifeCycle(org.hphc.schema.claim.v6.LifeCycleEnumType.SUBMITTED);
			}

			if(stgClSvc.getClmdAnsthMinCnt() != null){
				AnesthesiaMinutes.Minutes anesthesiaMinutes = new AnesthesiaMinutes.Minutes();
				minutes.getMinutes().add(anesthesiaMinutes);
				anesthesiaMinutes.setValue(stgClSvc.getClmdAnsthMinCnt().longValue());
				anesthesiaMinutes.setLifeCycle(org.hphc.schema.claim.v6.LifeCycleEnumType.CLAIMED);
			}
		}
	}

	/**
	 * @param stgClSvc
	 * @param service
	 * Add service billing unit count and treatment days count
	 */
	private void addCounts(StgClSvc stgClSvc, MedicalBehServiceLineType service) {

		if(stgClSvc.getSubmProcUnitCnt() != null
				|| stgClSvc.getClmdProcUnitCnt() != null
				|| stgClSvc.getAdjudApprProcUnitCnt() != null
				|| stgClSvc.getAdjudDenyProcUnitCnt() != null){
			
			Counts counts = new Counts();
			service.setCounts(counts);

			if(stgClSvc.getSubmProcUnitCnt() != null){				
				Count serviceCnt = new Count();  
				counts.getCounts().add(serviceCnt);
				LOGGER.debug("stgClSvc.getSubmProcUnitCnt() : " +stgClSvc.getSubmProcUnitCnt().setScale(4));
				
				serviceCnt.setValue(stgClSvc.getSubmProcUnitCnt().setScale(4));
				if(hasValue(stgClSvc.getPrcProcUomCd())){
					serviceCnt.setUnitOfMeasure(stgClSvc.getPrcProcUomCd().trim());
				}
				serviceCnt.setLifeCycle(org.hphc.schema.claim.v6.LifeCycleEnumType.SUBMITTED);
			}
			if(stgClSvc.getClmdProcUnitCnt() != null){
				Count serviceCnt = new Count();  
				counts.getCounts().add(serviceCnt);
				LOGGER.debug("stgClSvc.getClmdProcUnitCnt() : " +stgClSvc.getClmdProcUnitCnt().setScale(4));
				
				serviceCnt.setValue(stgClSvc.getClmdProcUnitCnt().setScale(4));
				if(hasValue(stgClSvc.getPrcProcUomCd())){
					serviceCnt.setUnitOfMeasure(stgClSvc.getPrcProcUomCd().trim());
				}
				serviceCnt.setLifeCycle(org.hphc.schema.claim.v6.LifeCycleEnumType.CLAIMED);
			}
			if(stgClSvc.getAdjudApprProcUnitCnt() != null){				
				Count serviceCnt = new Count();  
				counts.getCounts().add(serviceCnt);
				LOGGER.debug("stgClSvc.getAdjudApprProcUnitCnt() : " +stgClSvc.getAdjudApprProcUnitCnt().setScale(4));
				
				serviceCnt.setValue(stgClSvc.getAdjudApprProcUnitCnt().setScale(4));
				if(hasValue(stgClSvc.getPrcProcUomCd())){
					serviceCnt.setUnitOfMeasure(stgClSvc.getPrcProcUomCd().trim());
				}
				serviceCnt.setLifeCycle(org.hphc.schema.claim.v6.LifeCycleEnumType.ADJUDICATED);				
				serviceCnt.setStatus("APPROVED");
			}
			if(stgClSvc.getAdjudDenyProcUnitCnt() != null){
				Count serviceCnt = new Count();  
				counts.getCounts().add(serviceCnt);
				LOGGER.debug("stgClSvc.getAdjudDenyProcUnitCnt() : " +stgClSvc.getAdjudDenyProcUnitCnt().setScale(4));
				serviceCnt.setValue(stgClSvc.getAdjudDenyProcUnitCnt().setScale(4));
				if(hasValue(stgClSvc.getPrcProcUomCd())){
					serviceCnt.setUnitOfMeasure(stgClSvc.getPrcProcUomCd().trim());
				}
				serviceCnt.setLifeCycle(org.hphc.schema.claim.v6.LifeCycleEnumType.ADJUDICATED);
				serviceCnt.setStatus("DENIED");
			}
		}
	}

	/**
	 * @param stgClSvc
	 * @param service
	 */
	private void authorizationNumbersMedicalService(StgClSvc stgClSvc,
			MedicalBehServiceLineType service) {

		if(hasValue(stgClSvc.getAuth1Nbr()) 
				|| hasValue(stgClSvc.getAuth2Nbr()) 
				|| hasValue(stgClSvc.getAuth3Nbr())){
			AuthorizationNbrs authNbrs =  new AuthorizationNbrs();
			service.setAuthorizationNbrs(authNbrs);

			if(hasValue(stgClSvc.getAuth1Nbr())){
				authNbrs.getAuthorizationNbrs().add(stgClSvc.getAuth1Nbr().trim());
			}

			if(hasValue(stgClSvc.getAuth2Nbr())){
				authNbrs.getAuthorizationNbrs().add(stgClSvc.getAuth2Nbr().trim());
			}
			if(hasValue(stgClSvc.getAuth3Nbr())){
				authNbrs.getAuthorizationNbrs().add(stgClSvc.getAuth3Nbr().trim());
			}
		}

	}

	/**
	 * @param stgClSvc
	 * @param service
	 * Provider details of service line
	 */
	private void providersMedicalService(StgClSvc stgClSvc,
			MedicalBehServiceLineType service, CodeDecodeUtil codeDecodeUtil) {

		if(hasValue(stgClSvc.getSvcPvNbr())
				|| hasValue(stgClSvc.getRefPvNbr()) 
				|| hasValue(stgClSvc.getPcpPvNbr())
				|| hasValue(stgClSvc.getSvcPvCntrtAffNbr())
				||	hasValue(stgClSvc.getRefPvCntrtAffNbr())
				||	hasValue(stgClSvc.getPcpPvCntrtAffNbr())){
			
			ProvidersType providers = new ProvidersType();
			service.setProviders(providers);
			// Servicing Provider
			if(hasValue(stgClSvc.getSvcPvNbr())
					|| hasValue(stgClSvc.getSvcPvCntrtAffNbr())){
				
				ProviderType provider = new ProviderType();
				providers.getProviders().add(provider);
				provider.setRole(org.hphc.schema.claim.v6.ProviderRoleEnumType.SERVICING);
				
				Ids providerIds = new Ids();
				provider.setIds(providerIds);
				//Provider Id
				if(hasValue(stgClSvc.getSvcPvNbr())){
					ProviderType.Ids.Id pId = new ProviderType.Ids.Id();
					providerIds.getIds().add(pId);
					pId.setValue(stgClSvc.getSvcPvNbr().trim());
					pId.setIdSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
					pId.setRole(org.hphc.schema.claim.v6.ProviderRoleEnumType.SERVICING);
					pId.setIdType(ProviderIdTypeEnumType.PROVIDER);
				}
				//Affliation Id
				if(hasValue(stgClSvc.getSvcPvCntrtAffNbr())){
					ProviderType.Ids.Id pId = new ProviderType.Ids.Id();
					providerIds.getIds().add(pId);
					pId.setValue(stgClSvc.getSvcPvCntrtAffNbr().trim());
					pId.setIdSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
					pId.setRole(org.hphc.schema.claim.v6.ProviderRoleEnumType.SERVICING);
					pId.setIdType(ProviderIdTypeEnumType.AFFILIATION);
				}
				//Add Provider Name
				if(hasValue(stgClSvc.getProviderName())){
					PersonNameType nameType = new PersonNameType();
					provider.setName(nameType);
					nameType.setFormattedName(stgClSvc.getProviderName());
				}

				// Add Servicing Provider NPI
				if(hasValue(stgClSvc.getProviderNpi())){
					ProviderType.Ids.Id providerNpiId = new ProviderType.Ids.Id();
					providerIds.getIds().add(providerNpiId);					
					providerNpiId.setValue(stgClSvc.getProviderNpi());
					providerNpiId.setIdSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
					providerNpiId.setRole(org.hphc.schema.claim.v6.ProviderRoleEnumType.SERVICING);
					providerNpiId.setIdType(ProviderIdTypeEnumType.NPI); 
				}
				// Add Servicing Provider submitted NPI, 
				// TO 85780, Provider Billing NPI to Populate irrespective of Provider NPI at Service Line Level, for Medical Claims
				if(hasValue(stgClSvc.getSubmittedNpi())){
					ProviderType.Ids.Id providerNpiId = new ProviderType.Ids.Id();
					providerIds.getIds().add(providerNpiId);		
					providerNpiId.setValue(stgClSvc.getSubmittedNpi());
					providerNpiId.setIdSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
					providerNpiId.setRole(org.hphc.schema.claim.v6.ProviderRoleEnumType.SERVICING);
					providerNpiId.setIdType(ProviderIdTypeEnumType.BILLING_NPI); 
				}
				
				//Add Contract Status
				if(hasValue(stgClSvc.getAdjudPvNtwrkCd())){
					ProviderType.ContractStatus status = new ProviderType.ContractStatus();
					provider.setContractStatus(status);
					status.setCode(stgClSvc.getAdjudPvNtwrkCd().trim());					
				}
				
				//Add Contract Reporting Unit
				if(hasValue(stgClSvc.getSvcPvCsuCd())){
					ProviderType.ContractReportingUnit csu = new ProviderType.ContractReportingUnit();
					provider.setContractReportingUnit(csu);
					csu.setCode(stgClSvc.getSvcPvCsuCd().trim());
					csu.setValue(stgClSvc.getSvcPvCsuCd().trim());
				}
				//add  speciality			
				specialityCodeMedicalService(stgClSvc,provider,  codeDecodeUtil);	
				
				//add Drived code @service			
				if(hasValue(stgClSvc.getSvcOrigDerivSvcPvCd())){
					ProviderType.DerivedCode drivedCode = new ProviderType.DerivedCode();
					provider.setDerivedCode(drivedCode);
					drivedCode.setCode(stgClSvc.getSvcOrigDerivSvcPvCd().trim());
					drivedCode.setValue(codeDecodeUtil.decodeProviderDrivedCodes(stgClSvc.getSvcOrigDerivSvcPvCd().trim()));		
				}
			}

			//Refering Provider
			if(hasValue(stgClSvc.getRefPvNbr()) 
					|| hasValue(stgClSvc.getRefPvCntrtAffNbr())){
				ProviderType  provider= new ProviderType();
				providers.getProviders().add(provider);
				provider.setRole(org.hphc.schema.claim.v6.ProviderRoleEnumType.REFERRING);
				Ids providerIds = new Ids();
				provider.setIds(providerIds);
				//Refering Provider Id
				if(hasValue(stgClSvc.getRefPvNbr())){
					ProviderType.Ids.Id pId = new ProviderType.Ids.Id();
					providerIds.getIds().add(pId);
					pId.setValue(stgClSvc.getRefPvNbr().trim());
					pId.setIdSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
					pId.setRole(org.hphc.schema.claim.v6.ProviderRoleEnumType.REFERRING);
					pId.setIdType(ProviderIdTypeEnumType.PROVIDER);
				}
				//Affliation Id
				if(hasValue(stgClSvc.getRefPvCntrtAffNbr())){
					ProviderType.Ids.Id pId = new ProviderType.Ids.Id();
					providerIds.getIds().add(pId);
					pId.setValue(stgClSvc.getRefPvCntrtAffNbr().trim());
					pId.setIdSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
					pId.setRole(org.hphc.schema.claim.v6.ProviderRoleEnumType.REFERRING);
					pId.setIdType(ProviderIdTypeEnumType.AFFILIATION);
				}

				//Add Referring Provider NPI
				if(hasValue(stgClSvc.getRefProviderNpi())){
					ProviderType.Ids.Id providerNpiId = new ProviderType.Ids.Id();
					providerIds.getIds().add(providerNpiId);					
					providerNpiId.setValue(stgClSvc.getRefProviderNpi());
					providerNpiId.setIdSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
					providerNpiId.setRole(org.hphc.schema.claim.v6.ProviderRoleEnumType.REFERRING);
					providerNpiId.setIdType(ProviderIdTypeEnumType.NPI); 
				}
				//add  speciality			
				//specialityCodeMedicalService(stgClSvc,provider);
			}

			if(hasValue(stgClSvc.getPcpPvNbr()) 
					|| hasValue(stgClSvc.getPcpPvCntrtAffNbr()) ){
				ProviderType  provider= new ProviderType();
				providers.getProviders().add(provider);
				provider.setRole(org.hphc.schema.claim.v6.ProviderRoleEnumType.PCP);				
				Ids providerIds = new Ids();
				provider.setIds(providerIds);

				if(hasValue(stgClSvc.getPcpPvNbr())){
					ProviderType.Ids.Id pId = new ProviderType.Ids.Id();
					providerIds.getIds().add(pId);
					pId.setValue(stgClSvc.getPcpPvNbr().trim());
					pId.setIdSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
					pId.setRole(org.hphc.schema.claim.v6.ProviderRoleEnumType.PCP);
					pId.setIdType(ProviderIdTypeEnumType.PROVIDER);
				}

				if(hasValue(stgClSvc.getPcpPvCntrtAffNbr())){
					ProviderType.Ids.Id pId = new ProviderType.Ids.Id();
					providerIds.getIds().add(pId);
					pId.setValue(stgClSvc.getPcpPvCntrtAffNbr().trim());
					pId.setIdSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
					pId.setRole(org.hphc.schema.claim.v6.ProviderRoleEnumType.PCP);
					pId.setIdType(ProviderIdTypeEnumType.AFFILIATION);
				}

				//Add PCP NPI
				if(hasValue(stgClSvc.getPcpProviderNpi())){
					ProviderType.Ids.Id providerNpiId = new ProviderType.Ids.Id();
					providerIds.getIds().add(providerNpiId);		
					providerNpiId.setValue(stgClSvc.getPcpProviderNpi());
					providerNpiId.setIdSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
					providerNpiId.setRole(org.hphc.schema.claim.v6.ProviderRoleEnumType.PCP);
					providerNpiId.setIdType(ProviderIdTypeEnumType.NPI); 
				}
				//add  speciality			
				//specialityCodeMedicalService(stgClSvc,provider);		
			}
		}

	}

	/**
	 * @param stgClSvc
	 * @param service
	 * Diagnosis code for Medical service lines
	 */
	private void addServiceLineDiagnosis(StgClSvc stgClSvc,
			MedicalBehServiceLineType service,CodeDecodeUtil codeDecodeUtil) {

		String codeSubStype = null;
		if(hasValue(stgClSvc.getDiag1Cd()) 
				||hasValue(stgClSvc.getDiag2Cd())
				||hasValue(stgClSvc.getDiag3Cd())
				||hasValue(stgClSvc.getDiag4Cd())){
			Diagnoses diagnoses = new Diagnoses();
			service.setDiagnoses(diagnoses);
			if(stgClSvc.getClaim() != null && stgClSvc.getClaim().getClIcdRevisNbr() != null){
				codeSubStype = "ICD-" + stgClSvc.getClaim().getClIcdRevisNbr();
			}
			if(hasValue(stgClSvc.getDiag1Cd())){
				DiagnosisType diagnosisType = new DiagnosisType();
				diagnoses.getDiagnosises().add(diagnosisType);
				diagnosisType.setIndex(new BigInteger("1"));
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				diagnosisType.getCodes().add(code);				
				code.setValue(stgClSvc.getDiag1Cd().trim());
				code.setIndex(new BigInteger("0"));
				code.setType(ClaimConstants.HIPAA);
				if(codeSubStype != null){
					code.setSubType(codeSubStype);
				}
				String diagDesc = codeDecodeUtil.decodeDiagnosis(stgClSvc.getDiag1Cd().trim(),ClaimConstants.CLAIM_SOURCE.OHI);
				if(hasValue(diagDesc)){
					code.setDescription(diagDesc);
				}
			}
			if(hasValue(stgClSvc.getDiag2Cd())){
				DiagnosisType diagnosisType = new DiagnosisType();
				diagnoses.getDiagnosises().add(diagnosisType);
				diagnosisType.setIndex(new BigInteger("2"));
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				diagnosisType.getCodes().add(code);
				code.setValue(stgClSvc.getDiag2Cd().trim());
				code.setIndex(new BigInteger("0"));
				code.setType(ClaimConstants.HIPAA);
				if(codeSubStype != null){
					code.setSubType(codeSubStype);
				}
				String diagDesc = codeDecodeUtil.decodeDiagnosis(stgClSvc.getDiag2Cd().trim(),ClaimConstants.CLAIM_SOURCE.OHI);
				if(hasValue(diagDesc)){
					code.setDescription(diagDesc);
				}
			}
			if(hasValue(stgClSvc.getDiag3Cd())){
				DiagnosisType diagnosisType = new DiagnosisType();
				diagnoses.getDiagnosises().add(diagnosisType);
				diagnosisType.setIndex(new BigInteger("3"));
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				diagnosisType.getCodes().add(code);
				code.setValue(stgClSvc.getDiag3Cd().trim());
				code.setIndex(new BigInteger("0"));
				code.setType(ClaimConstants.HIPAA);
				if(codeSubStype != null){
					code.setSubType(codeSubStype);
				}
				String diagDesc = codeDecodeUtil.decodeDiagnosis(stgClSvc.getDiag3Cd().trim(),ClaimConstants.CLAIM_SOURCE.OHI);
				if(hasValue(diagDesc)){
					code.setDescription(diagDesc);
				}
			}
			if(hasValue(stgClSvc.getDiag4Cd())){
				DiagnosisType diagnosisType = new DiagnosisType();
				diagnoses.getDiagnosises().add(diagnosisType);
				diagnosisType.setIndex(new BigInteger("4"));
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				diagnosisType.getCodes().add(code);
				code.setValue(stgClSvc.getDiag4Cd().trim());
				code.setIndex(new BigInteger("0"));
				code.setType(ClaimConstants.HIPAA);
				if(codeSubStype != null){
					code.setSubType(codeSubStype);
				}
				String diagDesc = codeDecodeUtil.decodeDiagnosis(stgClSvc.getDiag4Cd().trim(),ClaimConstants.CLAIM_SOURCE.OHI);
				if(hasValue(diagDesc)){
					code.setDescription(diagDesc);
				}
			}
		}
	}

	/**
	 * @param stgClSvc
	 * @param service
	 * Populate amounts for Medical claims service lines
	 */
	private void medicalServiceLineAmounts(StgClSvc stgClSvc,
			MedicalBehServiceLineType service,boolean isResultContentSummary) {

		Amounts amounts = new Amounts();
		service.setAmounts(amounts);
		//updated for an issue double the charge amount
		AmountType clmdAmountBilled = new AmountType();
		amounts.getAmounts().add(clmdAmountBilled);
		clmdAmountBilled.setType(org.hphc.schema.claim.v6.AmountTypeEnumType.BILLED_AMOUNT);		
		clmdAmountBilled.setValue(formatAmounts(stgClSvc.getClmdBillAmt(),stgClSvc.getClSvcReplInd() ));
		clmdAmountBilled.setLifeCycle(org.hphc.schema.claim.v6.LifeCycleEnumType.CLAIMED);
				
		
		AmountType submAmountBilled = new AmountType();
		amounts.getAmounts().add(submAmountBilled);
		submAmountBilled.setType(org.hphc.schema.claim.v6.AmountTypeEnumType.BILLED_AMOUNT);
		submAmountBilled.setValue(formatAmount(stgClSvc.getSubmBillAmt() ));
		submAmountBilled.setLifeCycle(org.hphc.schema.claim.v6.LifeCycleEnumType.SUBMITTED);

		AmountType amountCoIns = new AmountType();
		amounts.getAmounts().add(amountCoIns);
		amountCoIns.setType(org.hphc.schema.claim.v6.AmountTypeEnumType.COINSURANCE_AMOUNT);
		amountCoIns.setValue(formatAmounts(stgClSvc.getCoinsAmt(), stgClSvc.getClSvcReplInd() ));

		AmountType amountCoPay = new AmountType();
		amounts.getAmounts().add(amountCoPay);
		amountCoPay.setType(org.hphc.schema.claim.v6.AmountTypeEnumType.CO_PAY_AMOUNT);
		amountCoPay.setValue(formatAmounts(stgClSvc.getCopayAmt(), stgClSvc.getClSvcReplInd()));

		
		AmountType amountPaid = new AmountType();
		amounts.getAmounts().add(amountPaid);
		amountPaid.setType(org.hphc.schema.claim.v6.AmountTypeEnumType.PAID_AMOUNT);
		amountPaid.setValue(formatAmounts(stgClSvc.getPyblAmt(), stgClSvc.getClSvcReplInd()));

		
		AmountType amountDeductible = new AmountType();
		amounts.getAmounts().add(amountDeductible);
		amountDeductible.setType(org.hphc.schema.claim.v6.AmountTypeEnumType.DEDUCTIBLE_AMOUNT);
		amountDeductible.setValue(formatAmounts(stgClSvc.getDedAmt(), stgClSvc.getClSvcReplInd()));
		
		if(!isResultContentSummary){
			AmountType prcAmountDenied = new AmountType();
			amounts.getAmounts().add(prcAmountDenied);
			prcAmountDenied.setType(org.hphc.schema.claim.v6.AmountTypeEnumType.DENIED_AMOUNT);
			prcAmountDenied.setValue(formatAmounts(stgClSvc.getPrcDenyAmt(), stgClSvc.getClSvcReplInd()));
			prcAmountDenied.setLifeCycle(org.hphc.schema.claim.v6.LifeCycleEnumType.PRICED);

			AmountType adjAmountDenied = new AmountType();
			amounts.getAmounts().add(adjAmountDenied);
			adjAmountDenied.setType(org.hphc.schema.claim.v6.AmountTypeEnumType.DENIED_AMOUNT);
			adjAmountDenied.setValue(formatAmounts(stgClSvc.getAdjudDenyAmt(), stgClSvc.getClSvcReplInd()));
			adjAmountDenied.setLifeCycle(org.hphc.schema.claim.v6.LifeCycleEnumType.ADJUDICATED);

			AmountType prcAmountAllowed = new AmountType();
			amounts.getAmounts().add(prcAmountAllowed);
			prcAmountAllowed.setType(org.hphc.schema.claim.v6.AmountTypeEnumType.ALLOWED_AMOUNT);
			prcAmountAllowed.setValue(formatAmounts(stgClSvc.getOhiAllowAmt(), stgClSvc.getClSvcReplInd()));
			
			AmountType adjAmountAllowed = new AmountType();
			amounts.getAmounts().add(adjAmountAllowed);
			adjAmountAllowed.setType(org.hphc.schema.claim.v6.AmountTypeEnumType.BENEFIT_INPUT_ALLOWED_AMOUNT);
			adjAmountAllowed.setValue(formatAmounts(stgClSvc.getBnftNputAmt(), stgClSvc.getClSvcReplInd()));
			
			AmountType amountDiscountRate = new AmountType();
			amounts.getAmounts().add(amountDiscountRate);
			amountDiscountRate.setType(org.hphc.schema.claim.v6.AmountTypeEnumType.DISCOUNT_AMOUNT);
			amountDiscountRate.setValue(formatAmounts(stgClSvc.getDiscAmt(), stgClSvc.getClSvcReplInd()));

			AmountType riskRate = new AmountType();
			amounts.getAmounts().add(riskRate);
			riskRate.setType(org.hphc.schema.claim.v6.AmountTypeEnumType.RISK_AMOUNT);
			riskRate.setValue(formatAmounts(stgClSvc.getRiskWhldAmt(), stgClSvc.getClSvcReplInd()));

			AmountType cmsAllowed = new AmountType();
			amounts.getAmounts().add(cmsAllowed);
			cmsAllowed.setType(org.hphc.schema.claim.v6.AmountTypeEnumType.CMS_ALLOWED_AMOUNT);
			cmsAllowed.setValue(formatAmounts(stgClSvc.getCmsAllowAmt(), stgClSvc.getClSvcReplInd()));

			AmountType cmsPaid = new AmountType();
			amounts.getAmounts().add(cmsPaid);
			cmsPaid.setType(org.hphc.schema.claim.v6.AmountTypeEnumType.CMS_PAYMENT_AMOUNT);
			cmsPaid.setValue(formatAmounts(stgClSvc.getCmsPaidAmt(),stgClSvc.getClSvcReplInd()));

			AmountType thirdPartyPaid = new AmountType();
			amounts.getAmounts().add(thirdPartyPaid);
			thirdPartyPaid.setType(org.hphc.schema.claim.v6.AmountTypeEnumType.THIRD_PARTY_AMOUNT);
			thirdPartyPaid.setValue(formatAmounts(stgClSvc.getPrecPayerPaidAmt(), stgClSvc.getClSvcReplInd())); 

			
			AmountType amountPenality = new AmountType();
			amounts.getAmounts().add(amountPenality);
			amountPenality.setType(org.hphc.schema.claim.v6.AmountTypeEnumType.PENALTY_AMOUNT);
			amountPenality.setValue(formatAmounts(stgClSvc.getMbPnltyAmt(), stgClSvc.getClSvcReplInd())); 

			AmountType memberLiability = new AmountType();
			amounts.getAmounts().add(memberLiability);
			memberLiability.setType(org.hphc.schema.claim.v6.AmountTypeEnumType.MEMBER_LIABILITY_AMOUNT);
			memberLiability.setValue(formatAmounts(stgClSvc.getMbLiabAmt(), stgClSvc.getClSvcReplInd()));

			AmountType providerLiability = new AmountType();
			amounts.getAmounts().add(providerLiability);
			providerLiability.setType(org.hphc.schema.claim.v6.AmountTypeEnumType.PROVIDER_LIABILITY_AMOUNT);
			providerLiability.setValue(formatAmounts(stgClSvc.getPvLiabAmt(), stgClSvc.getClSvcReplInd())); 

			
			AmountType capitation = new AmountType();
			amounts.getAmounts().add(capitation);
			capitation.setType(org.hphc.schema.claim.v6.AmountTypeEnumType.CAPITATION_AMOUNT);
			capitation.setValue(formatAmounts(stgClSvc.getFfsAmt(), stgClSvc.getClSvcReplInd())); 
			
			AmountType authNotSufficient = new AmountType();
			amounts.getAmounts().add(authNotSufficient);
			authNotSufficient.setType(org.hphc.schema.claim.v6.AmountTypeEnumType.AUTH_NOT_SUFFICIENT_AMOUNT);
			authNotSufficient.setValue(formatAmounts(stgClSvc.getAuthNotSufntAmt(), stgClSvc.getClSvcReplInd()));
			
			AmountType overBnftLimit = new AmountType();
			amounts.getAmounts().add(overBnftLimit);
			overBnftLimit.setType(org.hphc.schema.claim.v6.AmountTypeEnumType.OVER_BENEFIT_LIMIT_AMOUNT);
			overBnftLimit.setValue(formatAmounts(stgClSvc.getOverBnftLimitAmt(), stgClSvc.getClSvcReplInd()));


			AmountType preceedingPayerCoinsuranceAmount = new AmountType();
			amounts.getAmounts().add(preceedingPayerCoinsuranceAmount);
			preceedingPayerCoinsuranceAmount.setType(org.hphc.schema.claim.v6.AmountTypeEnumType.PRECEDING_PAYER_COINSURANCE_AMOUNT);
			preceedingPayerCoinsuranceAmount.setValue(formatAmounts(stgClSvc.getPrecPayerCoinsPaidAmt(), stgClSvc.getClSvcReplInd()));
			
			AmountType preceedingPayerDeductibleAmount = new AmountType();
			amounts.getAmounts().add(preceedingPayerDeductibleAmount);
			preceedingPayerDeductibleAmount.setType(org.hphc.schema.claim.v6.AmountTypeEnumType.PRECEDING_PAYER_DEDUCTIBLE_AMOUNT);
			preceedingPayerDeductibleAmount.setValue(formatAmounts(stgClSvc.getPrecPayerDedPaidAmt(), stgClSvc.getClSvcReplInd() ));
			
			
			AmountType preceedingPayerCopayAmount = new AmountType();
			amounts.getAmounts().add(preceedingPayerCopayAmount);
			preceedingPayerCopayAmount.setType(org.hphc.schema.claim.v6.AmountTypeEnumType.PRECEDING_PAYER_COPAY_AMOUNT);
			preceedingPayerCopayAmount.setValue(formatAmounts(stgClSvc.getPrecPayerCopayPaidAmt(), stgClSvc.getClSvcReplInd()));
		}
	}

	/**
	 * @param stgClSvc
	 * @param service
	 * Service line Procedure codes
	 */
	private void procedureCodeMedicalService(StgClSvc stgClSvc,
			MedicalBehServiceLineType service,CodeDecodeUtil codeDecodeUtil) {
		if(hasValue(stgClSvc.getClmdCptCd())
				|| hasValue(stgClSvc.getClmdRevCd())
				|| hasValue(stgClSvc.getSubmCptCd())
				|| hasValue(stgClSvc.getSubmRevCd())
				|| hasValue(stgClSvc.getPrcProcCd())){			
			Procedures procedures = new Procedures();
			service.setProcedures(procedures);
			
			Date serviceStartDate = null;			
			if(stgClSvc.getSvcBegDt() != null)
				serviceStartDate=stgClSvc.getSvcBegDt();
			else
				serviceStartDate= new Date();
			
			if(hasValue(stgClSvc.getClmdCptCd())){				
				Procedure pCode = new Procedure();
				procedures.getProcedures().add(pCode);				
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				pCode.getCodes().add(code);
				code.setValue(stgClSvc.getClmdCptCd().trim());
				code.setType("HIPAA");
				code.setSubType("CPT4");
				CodeSet procCode = codeDecodeUtil.getDecodedCodeProcedure(Constants.CODE.PROCEDURE,stgClSvc.getClmdCptCd().trim(),serviceStartDate,ClaimConstants.CLAIM_SOURCE.OHI);
				if(procCode != null && hasValue(procCode.getDescription())){
					code.setDescription(procCode.getDescription());
				}
				if(procCode != null && hasValue(procCode.getShortDescription())){
					code.setShortDescription(procCode.getShortDescription());
				}
				code.setSource(ClaimConstants.CLAIM_SOURCE.OHI.toString().toString());					
				pCode.setLifeCycle(org.hphc.schema.claim.v6.LifeCycleEnumType.CLAIMED);
			}
			if(hasValue(stgClSvc.getClmdRevCd())){
				Procedure pCode = new Procedure();
				procedures.getProcedures().add(pCode);
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				pCode.getCodes().add(code);
				code.setValue(stgClSvc.getClmdRevCd().trim());
				code.setSubType("REV");
				code.setType("HIPAA");
				CodeSet procCode = codeDecodeUtil.getDecodedCodeProcedure(Constants.CODE.PROCEDURE,stgClSvc.getClmdRevCd().trim(),serviceStartDate,ClaimConstants.CLAIM_SOURCE.OHI);
				if(procCode != null && hasValue(procCode.getDescription())){
					code.setDescription(procCode.getDescription());
				}
				if(procCode != null && hasValue(procCode.getShortDescription())){
					code.setShortDescription(procCode.getShortDescription());
				}				
				code.setSource(ClaimConstants.CLAIM_SOURCE.OHI.toString().toString());
				pCode.setLifeCycle(org.hphc.schema.claim.v6.LifeCycleEnumType.CLAIMED);
			}
			
			if(hasValue(stgClSvc.getClmdCptCd())){
				Procedure pCode = new Procedure();
				procedures.getProcedures().add(pCode);
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				pCode.getCodes().add(code);
				code.setValue(stgClSvc.getClmdCptCd().trim());
				code.setType("HIPAA");
				code.setSubType("CPT4");
				CodeSet procCode = codeDecodeUtil.getDecodedCodeProcedure(Constants.CODE.PROCEDURE,stgClSvc.getClmdCptCd().trim(),serviceStartDate,ClaimConstants.CLAIM_SOURCE.OHI);
				if(procCode != null && hasValue(procCode.getDescription())){
					code.setDescription(procCode.getDescription());
				}
				if(procCode != null && hasValue(procCode.getShortDescription())){
					code.setShortDescription(procCode.getShortDescription());
				}
				code.setSource(ClaimConstants.CLAIM_SOURCE.OHI.toString().toString());
				pCode.setLifeCycle(org.hphc.schema.claim.v6.LifeCycleEnumType.ADJUDICATED);
				
			}
			if(hasValue(stgClSvc.getClmdRevCd())){
				Procedure pCode = new Procedure();
				procedures.getProcedures().add(pCode);
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				pCode.getCodes().add(code);
				code.setValue(stgClSvc.getClmdRevCd().trim());
				code.setSubType("REV");
				code.setType("HIPAA");
				CodeSet procCode = codeDecodeUtil.getDecodedCodeProcedure(Constants.CODE.PROCEDURE,stgClSvc.getClmdRevCd().trim(),serviceStartDate,ClaimConstants.CLAIM_SOURCE.OHI);
				if(procCode != null && hasValue(procCode.getDescription())){
					code.setDescription(procCode.getDescription());
				}
				if(procCode != null && hasValue(procCode.getShortDescription())){
					code.setShortDescription(procCode.getShortDescription());
				}
				code.setSource(ClaimConstants.CLAIM_SOURCE.OHI.toString().toString());
				pCode.setLifeCycle(org.hphc.schema.claim.v6.LifeCycleEnumType.ADJUDICATED);
			}
			if(hasValue(stgClSvc.getSubmCptCd())){
				Procedure pCode = new Procedure();
				procedures.getProcedures().add(pCode);
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				pCode.getCodes().add(code);
				code.setValue(stgClSvc.getSubmCptCd().trim());
				code.setSubType("CPT4");
				code.setType("HIPAA");
				CodeSet procCode = codeDecodeUtil.getDecodedCodeProcedure(Constants.CODE.PROCEDURE,stgClSvc.getSubmCptCd().trim(),serviceStartDate,ClaimConstants.CLAIM_SOURCE.OHI);
				if(procCode != null && hasValue(procCode.getDescription())){
					code.setDescription(procCode.getDescription());
				}
				if(procCode != null && hasValue(procCode.getShortDescription())){
					code.setShortDescription(procCode.getShortDescription());
				}
				code.setSource(ClaimConstants.CLAIM_SOURCE.OHI.toString().toString());
				pCode.setLifeCycle(org.hphc.schema.claim.v6.LifeCycleEnumType.SUBMITTED);
			}
			if(hasValue(stgClSvc.getSubmRevCd())){
				Procedure pCode = new Procedure();
				procedures.getProcedures().add(pCode);
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				pCode.getCodes().add(code);
				code.setValue(stgClSvc.getSubmRevCd().trim());
				code.setSubType("REV");
				code.setType("HIPAA");
				CodeSet procCode = codeDecodeUtil.getDecodedCodeProcedure(Constants.CODE.PROCEDURE,stgClSvc.getSubmRevCd().trim(),serviceStartDate,ClaimConstants.CLAIM_SOURCE.OHI);
				if(procCode != null && hasValue(procCode.getDescription())){
					code.setDescription(procCode.getDescription());
				}
				if(procCode != null && hasValue(procCode.getShortDescription())){
					code.setShortDescription(procCode.getShortDescription());
				}
				code.setSource(ClaimConstants.CLAIM_SOURCE.OHI.toString().toString());
				pCode.setLifeCycle(org.hphc.schema.claim.v6.LifeCycleEnumType.SUBMITTED);
			}
			if(hasValue(stgClSvc.getPrcProcCd())){
				Procedure pCode = new Procedure();
				procedures.getProcedures().add(pCode);
				org.hphc.schema.claim.v6.DecodeValueType.Code code = new org.hphc.schema.claim.v6.DecodeValueType.Code();
				pCode.getCodes().add(code);
				code.setType("HIPAA");
				code.setValue(stgClSvc.getPrcProcCd().trim());
				CodeSet procCode = codeDecodeUtil.getDecodedCodeProcedure(Constants.CODE.PROCEDURE,stgClSvc.getPrcProcCd().trim(),serviceStartDate,ClaimConstants.CLAIM_SOURCE.OHI);
				if(procCode != null && hasValue(procCode.getDescription())){
					code.setDescription(procCode.getDescription());
				}
				if(procCode != null && hasValue(procCode.getShortDescription())){
					code.setShortDescription(procCode.getShortDescription());
				}
				code.setSource(ClaimConstants.CLAIM_SOURCE.OHI.toString().toString());
				pCode.setLifeCycle(org.hphc.schema.claim.v6.LifeCycleEnumType.PRICED);
			}
		}
	}

	/**
	 * @param claim
	 * @param mBEHClaim
	 * 
	 * Add memdical claims total amounts
	 */
	private void addMedicalAggregateAmounts(StgCl claim,
			MedicalBehClaimType mBEHClaim,boolean isResultContentSummary) {

		Set<StgClSvc> claimServices = claim.getServices();

		double adjDeniedTotalAmount = 0;
		double allowedTotalAmount = 0;
		double bnftAllowedTotalAmount = 0;
		double amountDiscountTotal = 0;
		double amountRiskTotal = 0;
		double amountCMSAllowed = 0;
		double amountCMSPaid = 0;
		double amountTPPPaid = 0;
		double mbrPenalityAmount = 0;
		double capitationAmount = 0;
	
		for (StgClSvc service : claimServices) {
			// condition added for double amount issue
			if(service.getClSvcReplInd() == null || !( ClaimConstants.SERVICE_REPLACED_LINE_IND== (service.getClSvcReplInd()))){
				if(service.getAdjudDenyAmt() != null){
					adjDeniedTotalAmount = adjDeniedTotalAmount + service.getAdjudDenyAmt().doubleValue();
				}
				if(service.getOhiAllowAmt() != null){
					allowedTotalAmount = allowedTotalAmount + service.getOhiAllowAmt().doubleValue();
				}
				if(service.getBnftNputAmt() != null){
					bnftAllowedTotalAmount = bnftAllowedTotalAmount + service.getBnftNputAmt().doubleValue();
				}
				if(service.getDiscAmt() != null){
					amountDiscountTotal = amountDiscountTotal + service.getDiscAmt().doubleValue();
				}
				if(service.getRiskWhldAmt() != null){
					amountRiskTotal = amountRiskTotal + service.getRiskWhldAmt().doubleValue();
				}
				if(service.getCmsAllowAmt() != null){
					amountCMSAllowed = amountCMSAllowed +  service.getCmsAllowAmt().doubleValue();
				}
				if(service.getCmsPaidAmt() != null){
					amountCMSPaid = amountCMSPaid +  service.getCmsPaidAmt().doubleValue();
				}
				if(service.getPrecPayerPaidAmt() != null){
					amountTPPPaid = amountTPPPaid +  service.getPrecPayerPaidAmt().doubleValue();
				}
				if(service.getMbPnltyAmt() != null){
					mbrPenalityAmount = mbrPenalityAmount + service.getMbPnltyAmt().doubleValue();
				}
				if(service.getFfsAmt() != null){
					capitationAmount = capitationAmount + service.getFfsAmt().doubleValue();
				}
			}
		}

		MedicalBehClaimType.TotalAmounts amounts = new MedicalBehClaimType.TotalAmounts();
		mBEHClaim.setTotalAmounts(amounts);
		if(claim.getClmdTotAmt() != null){
			AmountType claimedAmountBilled = new AmountType();
			amounts.getTotalAmounts().add(claimedAmountBilled);
			claimedAmountBilled.setType(org.hphc.schema.claim.v6.AmountTypeEnumType.BILLED_AMOUNT);
			claimedAmountBilled.setValue(formatAmount(new BigDecimal(claim.getClmdTotAmt().doubleValue())));
			claimedAmountBilled.setLifeCycle(org.hphc.schema.claim.v6.LifeCycleEnumType.CLAIMED);
		}

		if(claim.getSubmBillTotAmt() != null){
			AmountType submittedAmountBilled = new AmountType();
			amounts.getTotalAmounts().add(submittedAmountBilled);			
			submittedAmountBilled.setType(org.hphc.schema.claim.v6.AmountTypeEnumType.BILLED_AMOUNT);
			submittedAmountBilled.setValue(formatAmount(new BigDecimal(claim.getSubmBillTotAmt().doubleValue())));
			submittedAmountBilled.setLifeCycle(org.hphc.schema.claim.v6.LifeCycleEnumType.SUBMITTED);
		}

		if(claim.getCoinsTotAmt() != null){
			AmountType amountCoIns = new AmountType();
			amounts.getTotalAmounts().add(amountCoIns);
			amountCoIns.setType(org.hphc.schema.claim.v6.AmountTypeEnumType.COINSURANCE_AMOUNT);
			amountCoIns.setValue(formatAmount(new BigDecimal(claim.getCoinsTotAmt().doubleValue())));
		}

		if(claim.getCopayTotAmt() != null){
			AmountType amountCoPay = new AmountType();
			amounts.getTotalAmounts().add(amountCoPay);			
			amountCoPay.setType(org.hphc.schema.claim.v6.AmountTypeEnumType.CO_PAY_AMOUNT);
			amountCoPay.setValue(formatAmount(new BigDecimal(claim.getCopayTotAmt().doubleValue())));
		}

		if(claim.getPyblTotAmt() != null){
			AmountType amountPaid = new AmountType();
			amounts.getTotalAmounts().add(amountPaid);
			amountPaid.setType(org.hphc.schema.claim.v6.AmountTypeEnumType.PAID_AMOUNT);
			amountPaid.setValue(formatAmount(new BigDecimal(claim.getPyblTotAmt().doubleValue())));
		}
		
		if(claim.getDedTotAmt() != null){
			AmountType amountDeductible = new AmountType();
			amounts.getTotalAmounts().add(amountDeductible);			
			amountDeductible.setType(org.hphc.schema.claim.v6.AmountTypeEnumType.DEDUCTIBLE_AMOUNT);
			amountDeductible.setValue(formatAmount(new BigDecimal(claim.getDedTotAmt().doubleValue())));
		}
		
		// Joint Venture
		if(claim.getPrecPayerPaidTotAmt() != null){
			AmountType precPayerPaidTotal = new AmountType();
			amounts.getTotalAmounts().add(precPayerPaidTotal);			
			precPayerPaidTotal.setType(org.hphc.schema.claim.v6.AmountTypeEnumType.PRECEDING_PAYER_PAID_AMOUNT);
			precPayerPaidTotal.setValue(formatAmount(new BigDecimal(claim.getPrecPayerPaidTotAmt().doubleValue())));
		}
		
		if(claim.getPrecPayerAllowTotAmt() != null){
			AmountType precPayerPaidTotal = new AmountType();
			amounts.getTotalAmounts().add(precPayerPaidTotal);			
			precPayerPaidTotal.setType(org.hphc.schema.claim.v6.AmountTypeEnumType.PRECEDING_PAYER_ALLOWED_AMOUNT);
			precPayerPaidTotal.setValue(formatAmount(new BigDecimal(claim.getPrecPayerAllowTotAmt().doubleValue())));
		}
		
		if(claim.getPrecPayerCoinsPaidTotAmt() != null){
			AmountType precPayerPaidTotal = new AmountType();
			amounts.getTotalAmounts().add(precPayerPaidTotal);			
			precPayerPaidTotal.setType(org.hphc.schema.claim.v6.AmountTypeEnumType.PRECEDING_PAYER_COINSURANCE_AMOUNT);
			precPayerPaidTotal.setValue(formatAmount(new BigDecimal(claim.getPrecPayerCoinsPaidTotAmt().doubleValue())));
		}
		
		if(claim.getPrecPayerDedPaidTotAmt() != null){
			AmountType precPayerPaidTotal = new AmountType();
			amounts.getTotalAmounts().add(precPayerPaidTotal);			
			precPayerPaidTotal.setType(org.hphc.schema.claim.v6.AmountTypeEnumType.PRECEDING_PAYER_DEDUCTIBLE_AMOUNT);
			precPayerPaidTotal.setValue(formatAmount(new BigDecimal(claim.getPrecPayerDedPaidTotAmt().doubleValue())));
		}
		
		if(claim.getPrecPayerCopayPaidTotAmt() != null){
			AmountType precPayerPaidTotal = new AmountType();
			amounts.getTotalAmounts().add(precPayerPaidTotal);			
			precPayerPaidTotal.setType(org.hphc.schema.claim.v6.AmountTypeEnumType.PRECEDING_PAYER_COPAY_AMOUNT);
			precPayerPaidTotal.setValue(formatAmount(new BigDecimal(claim.getPrecPayerCopayPaidTotAmt().doubleValue())));
		}


		AmountType amountInterest = new AmountType();
		amounts.getTotalAmounts().add(amountInterest);		
		amountInterest.setType(org.hphc.schema.claim.v6.AmountTypeEnumType.INTEREST_AMOUNT);
		amountInterest.setValue(formatAmount(claim.getInterestAmount()));
		
		AmountType amountAllowed = new AmountType();
		amounts.getTotalAmounts().add(amountAllowed);		
		amountAllowed.setType(org.hphc.schema.claim.v6.AmountTypeEnumType.ALLOWED_AMOUNT);
		amountAllowed.setValue(formatAmount(new BigDecimal(allowedTotalAmount)));
		
		
		
		
		if(!isResultContentSummary){
			AmountType adjudicatedAmountDenied = new AmountType();
			amounts.getTotalAmounts().add(adjudicatedAmountDenied);	
			adjudicatedAmountDenied.setType(org.hphc.schema.claim.v6.AmountTypeEnumType.DENIED_AMOUNT);
			adjudicatedAmountDenied.setValue(formatAmount(new BigDecimal(adjDeniedTotalAmount)));
			adjudicatedAmountDenied.setLifeCycle(org.hphc.schema.claim.v6.LifeCycleEnumType.ADJUDICATED);

			if(claim.getPrcDenyTotAmt() != null){
				AmountType pricedAmountDenied = new AmountType();
				amounts.getTotalAmounts().add(pricedAmountDenied);	
				pricedAmountDenied.setType(org.hphc.schema.claim.v6.AmountTypeEnumType.DENIED_AMOUNT);
				pricedAmountDenied.setValue(formatAmount(new BigDecimal(claim.getPrcDenyTotAmt().doubleValue())));
				pricedAmountDenied.setLifeCycle(org.hphc.schema.claim.v6.LifeCycleEnumType.PRICED);
			}
			
			
			AmountType amountBnftAllowed = new AmountType();
			amounts.getTotalAmounts().add(amountBnftAllowed);	
			amountBnftAllowed.setType(org.hphc.schema.claim.v6.AmountTypeEnumType.BENEFIT_INPUT_ALLOWED_AMOUNT);
			amountBnftAllowed.setValue(formatAmount(new BigDecimal(bnftAllowedTotalAmount)));

			AmountType amountDiscountRate = new AmountType();
			amounts.getTotalAmounts().add(amountDiscountRate);	
			amountDiscountRate.setType(org.hphc.schema.claim.v6.AmountTypeEnumType.DISCOUNT_AMOUNT);
			amountDiscountRate.setValue(formatAmount(new BigDecimal(amountDiscountTotal)));

			
			AmountType riskRate = new AmountType();
			amounts.getTotalAmounts().add(riskRate);	
			riskRate.setType(org.hphc.schema.claim.v6.AmountTypeEnumType.RISK_AMOUNT);
			riskRate.setValue(formatAmount(new BigDecimal(amountRiskTotal)));

			
			AmountType cmsAllowed = new AmountType();
			amounts.getTotalAmounts().add(cmsAllowed);	
			cmsAllowed.setType(org.hphc.schema.claim.v6.AmountTypeEnumType.CMS_ALLOWED_AMOUNT);
			cmsAllowed.setValue(formatAmount(new BigDecimal(amountCMSAllowed)));

			
			AmountType cmsPaid = new AmountType();
			amounts.getTotalAmounts().add(cmsPaid);	
			cmsPaid.setType(org.hphc.schema.claim.v6.AmountTypeEnumType.CMS_PAYMENT_AMOUNT);
			cmsPaid.setValue(formatAmount(new BigDecimal(amountCMSPaid)));

			AmountType thirdPartyPaid = new AmountType();
			amounts.getTotalAmounts().add(thirdPartyPaid);	
			thirdPartyPaid.setType(org.hphc.schema.claim.v6.AmountTypeEnumType.THIRD_PARTY_AMOUNT);
			thirdPartyPaid.setValue(formatAmount(new BigDecimal(amountTPPPaid)));

			
			AmountType amountMbrPenality = new AmountType();
			amounts.getTotalAmounts().add(amountMbrPenality);	
			amountMbrPenality.setType(org.hphc.schema.claim.v6.AmountTypeEnumType.PENALTY_AMOUNT);
			amountMbrPenality.setValue(formatAmount(new BigDecimal(mbrPenalityAmount)));

			if(claim.getMbLiabTotAmt() != null){
				AmountType amountMemberrLiablity = new AmountType();
				amounts.getTotalAmounts().add(amountMemberrLiablity);	
				amountMemberrLiablity.setType(org.hphc.schema.claim.v6.AmountTypeEnumType.MEMBER_LIABILITY_AMOUNT);
				amountMemberrLiablity.setValue(formatAmount(new BigDecimal(claim.getMbLiabTotAmt().doubleValue())));
			}

			if(claim.getPvLiabTotAmt() != null){
				AmountType amountProviderLiablity = new AmountType();
				amounts.getTotalAmounts().add(amountProviderLiablity);	
				amountProviderLiablity.setType(org.hphc.schema.claim.v6.AmountTypeEnumType.PROVIDER_LIABILITY_AMOUNT);
				amountProviderLiablity.setValue(formatAmount(new BigDecimal(claim.getPvLiabTotAmt().doubleValue())));
			}

			AmountType amountCapitation = new AmountType();
			amounts.getTotalAmounts().add(amountCapitation);	
			amountCapitation.setType(org.hphc.schema.claim.v6.AmountTypeEnumType.CAPITATION_AMOUNT);
			amountCapitation.setValue(formatAmount(new BigDecimal(capitationAmount)));
			
			AmountType authNotSufficient = new AmountType();
			amounts.getTotalAmounts().add(authNotSufficient);	
			authNotSufficient.setType(org.hphc.schema.claim.v6.AmountTypeEnumType.AUTH_NOT_SUFFICIENT_AMOUNT);
			authNotSufficient.setValue(formatAmount(claim.getAuthNotSufntTotAmt()));
			
			
			AmountType overBnftLimit = new AmountType();
			amounts.getTotalAmounts().add(overBnftLimit);	
			overBnftLimit.setType(org.hphc.schema.claim.v6.AmountTypeEnumType.OVER_BENEFIT_LIMIT_AMOUNT);
			overBnftLimit.setValue(formatAmount(claim.getOverBnftLimitTotAmt()));
		}
	}

	/**
	 * Fetch check details for claim
	 * @param stgCls
	 * @return map of claimnumbers and check details for each claim
	 */
	private Map<String, Set<HphcFileClaimInvoiceV>> getCheckDetails(List<StgCl> stgCls) {

		QueryBudgetContext qbc = LoggingThreadContext.getQueryBudgetContext();
		Map<String, Set<HphcFileClaimInvoiceV>> claimInvoiceMap = null;
		List<String> claimNumbers = new ArrayList<String>();
		for (StgCl stgCl : stgCls){
			claimNumbers.add(stgCl.getOhiClNbr().trim());
		}
		long starttime = System.currentTimeMillis();
		List<HphcFileClaimInvoiceV> invoiceList = invoiceDao.getPaymentDetailsByClaimNumber(claimNumbers);
		qbc.setOhiPaymentDetailTime((System.currentTimeMillis() - starttime));
		if(invoiceList != null && invoiceList.size() > 0){
			claimInvoiceMap = new HashMap<String, Set<HphcFileClaimInvoiceV>>();

			for (HphcFileClaimInvoiceV invoice : invoiceList){
				Set<HphcFileClaimInvoiceV> checkDetailsList = claimInvoiceMap.get(invoice.getClaimCode());
				if(checkDetailsList == null){
					checkDetailsList = new HashSet<HphcFileClaimInvoiceV>();
				}
				checkDetailsList.add(invoice);
				claimInvoiceMap.put(invoice.getClaimCode().trim(), checkDetailsList);
			}
		}

		return claimInvoiceMap;
	}
	
	/**
	 * Fetch check details for denied and encounter claims
	 * @param stgCls
	 * @return map of claimnumbers and check details for each claim
	 */
	private Map<String, Set<DfltPayeeClaimInvoice>> getDeniedClaimCheckDetails(List<StgCl> stgCls) {

		QueryBudgetContext qbc = LoggingThreadContext.getQueryBudgetContext();
		Map<String, Set<DfltPayeeClaimInvoice>> claimInvoiceMap = null;
		List<String> claimNumbers = new ArrayList<String>();
		for (StgCl stgCl : stgCls){
			claimNumbers.add(stgCl.getOhiClNbr().trim());
		}
		long starttime = System.currentTimeMillis();
		List<DfltPayeeClaimInvoice> invoiceList = invoiceDao.getEncntrDeniedClaimPaymentDetailsByClaimNumber(claimNumbers);
		qbc.setOhiDeniedClaimPaymentDetailTime((System.currentTimeMillis() - starttime));
		if(invoiceList != null && invoiceList.size() > 0){
			claimInvoiceMap = new HashMap<String, Set<DfltPayeeClaimInvoice>>();

			for (DfltPayeeClaimInvoice invoice : invoiceList){
				Set<DfltPayeeClaimInvoice> checkDetailsList = claimInvoiceMap.get(invoice.getOhiClNbr().trim());
				if(checkDetailsList == null){
					checkDetailsList = new HashSet<DfltPayeeClaimInvoice>();
				}
				checkDetailsList.add(invoice);
				claimInvoiceMap.put(invoice.getOhiClNbr().trim(), checkDetailsList);
			}
		}

		return claimInvoiceMap;
	}
	/**
	 * Fetch ApNoChk details
	 * @param stgCls
	 * @return map of claimnumbers and check details for each claim
	 */
	private Map<String, Set<ApNoChkPayeeClaimInvoice>> getApNoChkClaimDetails(List<StgCl> stgCls){
		QueryBudgetContext qbc = LoggingThreadContext.getQueryBudgetContext();
		Map<String, Set<ApNoChkPayeeClaimInvoice>> apNoChkClaimInvoiceMap = null;
		List<String> claimNumbers = new ArrayList<String>();
		for (StgCl stgCl : stgCls){
			claimNumbers.add(stgCl.getOhiClNbr().trim());
		}
		long starttime = System.currentTimeMillis();
		List<ApNoChkPayeeClaimInvoice> invoiceList = invoiceDao.getApNoChkClaimPaymentDetailsByClaimNumber(claimNumbers);
		qbc.setOhiApNoChkClaimPaymentDetailTime((System.currentTimeMillis() - starttime));
		if(invoiceList != null && invoiceList.size() > 0){
			apNoChkClaimInvoiceMap = new HashMap<String, Set<ApNoChkPayeeClaimInvoice>>();

			for (ApNoChkPayeeClaimInvoice invoice : invoiceList){
				Set<ApNoChkPayeeClaimInvoice> checkDetailsList = apNoChkClaimInvoiceMap.get(invoice.getOhiClNbr().trim());
				if(checkDetailsList == null){
					checkDetailsList = new HashSet<ApNoChkPayeeClaimInvoice>();
				}
				checkDetailsList.add(invoice);
				apNoChkClaimInvoiceMap.put(invoice.getOhiClNbr().trim(), checkDetailsList);
			}
		}

		return apNoChkClaimInvoiceMap;
	}
	
	/**
	 * @param stgClSvc
	 * @param service
	 * @param relatedClaimsFlag
	 * @param claimInvoiceMap
	 * @param deniedClaimInvoiceMap
	 */
	private void addCheckDetails(StgClSvc stgClSvc,
			MedicalBehServiceLineType service, boolean relatedClaimsFlag,Map<String, Set<HphcFileClaimInvoiceV>> claimInvoiceMap, Map<String, Set<DfltPayeeClaimInvoice>> deniedClaimInvoiceMap,Map<String, Set<ApNoChkPayeeClaimInvoice>>apNoChkClaimInvoiceMap) {

		Set<HphcFileClaimInvoiceV> approvedClaimChecks = null;
		Set<DfltPayeeClaimInvoice> deniedClaimChecks = null;
		HphcFileClaimPaymentV approvedCheck = null;
		DfltPayeeClaimInvoice deniedCheck = null;
		boolean approvedClaimPaymentDetails = false;
		boolean deniedClaimPaymentDetails = false;
		//String negativeBalanceInd = null;
		Set<ApNoChkPayeeClaimInvoice> apNochkClaim =  null;
		ApNoChkPayeeClaimInvoice apNocheck =  null;
		boolean apNoChkClaimPaymentDetails =  false;

		if(claimInvoiceMap != null && claimInvoiceMap.size() > 0){
			approvedClaimChecks = claimInvoiceMap.get(stgClSvc.getClaim().getOhiClNbr().trim());
			//GetChecks for that service line version
			if(approvedClaimChecks != null && approvedClaimChecks.size() > 0){
				LOGGER.debug("Approved checks......");
				for(HphcFileClaimInvoiceV invoice : approvedClaimChecks){
					if(invoice != null && invoice.getHphcFileClaimPaymentVs() != null){
						for (HphcFileClaimPaymentV check : invoice.getHphcFileClaimPaymentVs()) {
					
							if(stgClSvc.getPmtNbr() != null 
									&& check != null 
									&& stgClSvc.getPmtNbr().equals(Long.valueOf(check.getCheckNumber()).toString())){
							
							
								approvedClaimPaymentDetails = true;
								approvedCheck = check;
							}
						}
					}
				}
			}
		}
		
		/*if(claimInvoiceMap != null && claimInvoiceMap.size() > 0){
			BigDecimal nonCreditLookupAmount= new BigDecimal(0.0);
			BigDecimal lookupCreditAmount= new BigDecimal(0.0);
			
			approvedClaimChecks = claimInvoiceMap.get(stgClSvc.getClaim().getOhiClNbr().trim());
			//GetChecks for that service line version
			if(approvedClaimChecks != null && approvedClaimChecks.size() > 0){
				LOGGER.info("Approved Claim Check Size : " + approvedClaimChecks.size()) ;
				LOGGER.info("Approved checks......");
				for(HphcFileClaimInvoiceV invoice : approvedClaimChecks){
					LOGGER.info("Invoice size:" +invoice.getHphcFileClaimPaymentVs().size());
					if(invoice != null && invoice.getHphcFileClaimPaymentVs() != null){
						for (HphcFileClaimPaymentV check : invoice.getHphcFileClaimPaymentVs()) {
							
							if(stgClSvc.getPmtNbr() != null 
									&& check != null 
									&& stgClSvc.getPmtNbr().equals(Long.valueOf(check.getCheckNumber()).toString())){
								
								approvedClaimPaymentDetails = true;
								approvedCheck = check;
							}
							 
							 //check invoiceTypeLookup Code
							 if(check.getInvoiceTypeLookupCode().equalsIgnoreCase("credit")){
								LOGGER.info("Credit Invoice_Id::" +check.getHphcFileClaimInvoiceV().getInvoiceId() +"Credit Invoice_Amount" +check.getInvoiceAmount());
								lookupCreditAmount= lookupCreditAmount.add(check.getInvoiceAmount().abs());
							 } else{
								 LOGGER.info("Non Credit Invoice_Id::" +check.getHphcFileClaimInvoiceV().getInvoiceId() +"Non Credit Invoice_Amount" +check.getInvoiceAmount());
								nonCreditLookupAmount = nonCreditLookupAmount.add(check.getInvoiceAmount());
							}
						}
						//condition for Negative bal.indicator
						if(lookupCreditAmount.compareTo(nonCreditLookupAmount)>1){							
							 negativeBalanceInd="y";
							 LOGGER.info("NegativeBalIndicator: Y");
						}else{
							 negativeBalanceInd="N";
							 LOGGER.info("NegativeBalIndicator: N");
						}
					}
				}
			}
			//condition for Negative bal.indicator
			if(lookupCreditAmount.compareTo(nonCreditLookupAmount)== 1){							
				 negativeBalanceInd="y";
				 LOGGER.info("NegativeBalIndicator: Y");
			}else{
				 negativeBalanceInd="N";
				 LOGGER.info("NegativeBalIndicator: N");
			}
		}*/
		if (deniedClaimInvoiceMap != null && deniedClaimInvoiceMap.size() > 0){
			deniedClaimChecks = deniedClaimInvoiceMap.get(stgClSvc.getClaim().getOhiClNbr().trim());
			//GetChecks for that service line version
			if(deniedClaimChecks != null && deniedClaimChecks.size() > 0){
				for(DfltPayeeClaimInvoice invoice : deniedClaimChecks){
					if(invoice != null && invoice.getCheckNbr() != null
							&& stgClSvc.getPmtNbr() != null 
							&& stgClSvc.getPmtNbr().trim().equals(invoice.getCheckNbr().trim())){
						deniedClaimPaymentDetails = true;
						deniedCheck = invoice;
					}
				}
			}
		}
		if (apNoChkClaimInvoiceMap != null && apNoChkClaimInvoiceMap.size() > 0){
			apNochkClaim = apNoChkClaimInvoiceMap.get(stgClSvc.getClaim().getOhiClNbr().trim());
			//GetChecks for that service line version
			if(apNochkClaim != null && apNochkClaim.size() > 0){
				for(ApNoChkPayeeClaimInvoice invoice : apNochkClaim){
					if(invoice != null && invoice.getCheckNbr() != null
							&& stgClSvc.getPmtNbr() != null 
							&& stgClSvc.getPmtNbr().trim().equals(invoice.getCheckNbr().trim())){
						apNoChkClaimPaymentDetails = true;
						apNocheck = invoice;
					}
				}
			}
		}
		
		if(approvedClaimPaymentDetails || deniedClaimPaymentDetails || apNoChkClaimPaymentDetails){			
			PaymentDetails paymentDetails = new PaymentDetails();
			service.setPaymentDetails(paymentDetails);
			if(approvedClaimPaymentDetails){
				addCafiCheckDetails(stgClSvc, paymentDetails, relatedClaimsFlag, approvedCheck);
			}
			if(deniedClaimPaymentDetails){
				addEncntrDeniedCheckDetails(stgClSvc,paymentDetails, relatedClaimsFlag, deniedCheck);
			}
			if(apNoChkClaimPaymentDetails){
				addApNoCheckDetails(stgClSvc,paymentDetails, relatedClaimsFlag, apNocheck);
			}
		}
	}

	/**
	 * @param stgClSvc
	 * @param paymentDetails
	 * @param relatedClaimsFlag
	 * @param checks
	 */
	private void addCafiCheckDetails(StgClSvc stgClSvc, PaymentDetails paymentDetails, boolean relatedClaimsFlag,
			HphcFileClaimPaymentV check) {

		LOGGER.debug("Entering into addCheckDetails");
		
		if(check != null){
			PaymentDetail paymentDetail = new PaymentDetail();
			paymentDetails.getPaymentDetails().add(paymentDetail);
			paymentDetail.setCheckNumber(Long.valueOf(check.getCheckNumber()).toString());
			if (check.getCheckDate() != null) {
				paymentDetail.setIssuedDate(XMLUtil.getXMLDate(check.getCheckDate()));
			}
			if (check.getClearedDate() != null) {
				paymentDetail.setClearedDate(XMLUtil.getXMLDate(check.getClearedDate()));
			}
			if (check.getCheckStatusLookupCode() != null) {
				MedicalBehServiceLineType.PaymentDetails.PaymentDetail.CheckStatusCode checkStatus = new MedicalBehServiceLineType.PaymentDetails.PaymentDetail.CheckStatusCode();
				paymentDetail.setCheckStatusCode(checkStatus);
				checkStatus.setCode(check.getCheckStatusLookupCode().trim());
				checkStatus.setValue(check.getCheckStatusLookupCode().trim());
			}
			if (check.getCheckAmount() != null) {
				paymentDetail.setTotalCheckAmount(formatAmount(check.getCheckAmount()).toString());
			}

			PayToType payToType = new PayToType();
			paymentDetail.setPayTo(payToType);
			if (hasValue(stgClSvc.getPrcPayeePayToCd())) {
				PayToType.Code payee = new PayToType.Code();
				payToType.setCode(payee);
				payee.setValue(Long.valueOf(check.getVendorId()).toString());
				payee.setCode(stgClSvc.getPrcPayeePayToCd());
				if (hasValue(stgClSvc.getPayeeNbr())) {
					payee.setId(stgClSvc.getPayeeNbr().trim());
				}
				payee.setCodeSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
			}
			

			if (hasValue(check.getNum1099())) {
				payToType.setProviderTin(check.getNum1099().trim());
			}
			
			if (hasValue(check.getVendorName())) {
				payToType.setName(check.getVendorName().trim());
			}
			LocationType address = new LocationType();
			payToType.setAddress(address);

			AddressLines addrLines = null ;
			// Only Add Address lines if we have an address or we are going to mess up the xml
			if(hasValue(check.getAddressLine1()) ||
					hasValue(check.getAddressLine2()) ||
					hasValue(check.getAddressLine3()) ||
					hasValue(check.getAddressLine4()))
			{
				addrLines = new AddressLines();
				address.setAddressLines(addrLines);
			}

			if (hasValue(check.getAddressLine1())) {
				AddressLine addrLine =new  AddressLine();
				addrLines.getAddressLines().add(addrLine);
				addrLine.setValue(check.getAddressLine1().trim());
				addrLine.setIndex(new BigInteger("1"));
			}
			if (hasValue(check.getAddressLine2())) {
				AddressLine addrLine =new  AddressLine();
				addrLines.getAddressLines().add(addrLine);
				addrLine.setValue(check.getAddressLine2().trim());
				addrLine.setIndex(new BigInteger("2"));
			}
			if (hasValue(check.getAddressLine3())) {
				AddressLine addrLine =new  AddressLine();
				addrLines.getAddressLines().add(addrLine);
				addrLine.setValue(check.getAddressLine3().trim());
				addrLine.setIndex(new BigInteger("3"));
			}
			if (hasValue(check.getAddressLine4())) {
				AddressLine addrLine =new  AddressLine();
				addrLines.getAddressLines().add(addrLine);
				addrLine.setValue(check.getAddressLine4().trim());
				addrLine.setIndex(new BigInteger("4"));
			}
			if (hasValue(check.getCity())) {
				address.setCityName(check.getCity().trim());
			}
			if (hasValue(check.getState())) {
				ObjectFactory objectFactory = new ObjectFactory();	
				address.setStateCode(objectFactory.createLocationTypeStateCode(check.getState().trim()));
			}
			if (hasValue(check.getCountry())) {
				address.setCountryCode((check.getCountry().trim()));
			}
			if (hasValue(check.getZip())) {
				address.setPostalCode((check.getZip().trim()));
			}
			
			//this may be place for negative bal.;
			  
			/*ValueType negativeBalIndicator = paymentDetail.addNewNegativeBalanceIndicator();
			 negativeBalIndicator.setCode(negativeBalanceIndicator);
			 negativeBalIndicator.setStringValue(negativeBalanceIndicator);
			 negativeBalIndicator.setCodeSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());*/

			////ValueType paymentMethod = paymentDetail.addNewPaymentMethod();
			if(hasValue(check.getPaymentMethodLookupCode())){
				MedicalBehServiceLineType.PaymentDetails.PaymentDetail.PaymentMethod paymentMethod = new MedicalBehServiceLineType.PaymentDetails.PaymentDetail.PaymentMethod();
				paymentDetail.setPaymentMethod(paymentMethod);
				paymentMethod.setValue(check.getPaymentMethodLookupCode());
				paymentMethod.setCode(check.getPaymentMethodLookupCode());
				paymentMethod.setCodeSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
			}
		}
	}

	/**
	 * @param stgClSvc
	 * @param paymentDetails
	 * @param relatedClaimsFlag
	 * @param checks
	 */
	private void addEncntrDeniedCheckDetails(StgClSvc stgClSvc,
			PaymentDetails paymentDetails, boolean relatedClaimsFlag, DfltPayeeClaimInvoice check) {

		LOGGER.debug("Entering into addCheckDetails");

		if(check != null){
			PaymentDetail paymentDetail = new PaymentDetail();
			paymentDetails.getPaymentDetails().add(paymentDetail);
			paymentDetail.setCheckNumber(check.getCheckNbr().trim());
			if(check.getCheckDate() != null){				
				paymentDetail.setIssuedDate(XMLUtil.getXMLDate(check.getCheckDate()));
			}
			if(check.getCheckAmt() != null){
				paymentDetail.setTotalCheckAmount(formatAmount(check.getCheckAmt()).toString());
			}

			PayToType payToType = new PayToType();
			paymentDetail.setPayTo(payToType);
			if(hasValue(stgClSvc.getPrcPayeePayToCd())){				
				PayToType.Code payee = new PayToType.Code();
				payToType.setCode(payee);
				payee.setValue(check.getPayeeNbr().trim());
				payee.setCode(stgClSvc.getPrcPayeePayToCd());
				if(hasValue(stgClSvc.getPayeeNbr())){
					payee.setId(stgClSvc.getPayeeNbr().trim());
				}
				payee.setCodeSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
			}
			
			if(hasValue(check.getTin())){
				payToType.setProviderTin(check.getTin().trim());
			}
			
			if(hasValue(check.getPayeeName())){
				payToType.setName(check.getPayeeName().trim());
			}
			
			LocationType address = new LocationType();
			payToType.setAddress(address);

			AddressLines addrLines = null ;
			// Only Add Address lines if we have an address or we are going to mess up the xml
			if( hasValue(check.getAddress1()) ||
					hasValue(check.getAddress2())  )
			{
				addrLines =  new AddressLines(); 
				address.setAddressLines(addrLines);
			}

			if(hasValue(check.getAddress1())){
				AddressLine addrLine = new AddressLine();
				addrLines.getAddressLines().add(addrLine);
				addrLine.setValue(check.getAddress1().trim());
				addrLine.setIndex(new BigInteger("1"));
			}
			if(hasValue(check.getAddress2())){
				AddressLine addrLine = new AddressLine();
				addrLines.getAddressLines().add(addrLine);
				addrLine.setValue(check.getAddress2().trim());
				addrLine.setIndex(new BigInteger("2"));
			}
			if(hasValue(check.getCity())){
				address.setCityName(check.getCity().trim());
			}
			if(hasValue(check.getState())){
				ObjectFactory objectFactory = new ObjectFactory();	
				address.setStateCode(objectFactory.createLocationTypeStateCode(check.getState().trim()));
			}
			if(hasValue(check.getZip())){
				address.setPostalCode((check.getZip().trim()));
			}
			
			if(hasValue(check.getPaymentType())){
				MedicalBehServiceLineType.PaymentDetails.PaymentDetail.PaymentMethod paymentMethod = new MedicalBehServiceLineType.PaymentDetails.PaymentDetail.PaymentMethod();
				paymentDetail.setPaymentMethod(paymentMethod);
				paymentMethod.setValue(check.getPaymentType());
				paymentMethod.setCode(check.getPaymentType());
				paymentMethod.setCodeSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
			}
		}
	}
	
	/**
	 * @param stgClSvc
	 * @param paymentDetails
	 * @param relatedClaimsFlag
	 * @param checks
	 */
	private void addApNoCheckDetails(StgClSvc stgClSvc,
			PaymentDetails paymentDetails, boolean relatedClaimsFlag, ApNoChkPayeeClaimInvoice check) {

		LOGGER.debug("Entering into addApNoCheckDetails");

		if(check != null){
			PaymentDetail paymentDetail = new PaymentDetail();
			paymentDetails.getPaymentDetails().add(paymentDetail);
			paymentDetail.setCheckNumber(check.getCheckNbr().trim());
			if(check.getCheckDate() != null){
				paymentDetail.setIssuedDate(XMLUtil.getXMLDate(check.getCheckDate()));
			}
			if(check.getCheckAmt() != null){
				paymentDetail.setTotalCheckAmount(formatAmount(check.getCheckAmt()).toString());
			}

			PayToType payToType = new PayToType();
			paymentDetail.setPayTo(payToType);
			if(hasValue(stgClSvc.getPrcPayeePayToCd())){
				PayToType.Code payee = new PayToType.Code();
				payToType.setCode(payee);
				payee.setValue(check.getPayeeNbr().trim());
				payee.setCode(stgClSvc.getPrcPayeePayToCd());
				if(hasValue(stgClSvc.getPayeeNbr())){
					payee.setId(stgClSvc.getPayeeNbr().trim());
				}
				payee.setCodeSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
			}
			
			if(hasValue(check.getTin())){
				payToType.setProviderTin(check.getTin().trim());
			}
			
			if(hasValue(check.getPayeeName())){
				payToType.setName(check.getPayeeName().trim());
			}
			
			LocationType address = new LocationType();
			payToType.setAddress(address);

			AddressLines addrLines = null ;
			// Only Add Address lines if we have an address or we are going to mess up the xml
			if( hasValue(check.getAddress1()) ||
					hasValue(check.getAddress2())  )
			{
				addrLines =  new AddressLines(); 
				address.setAddressLines(addrLines);
			}

			if(hasValue(check.getAddress1())){
				AddressLine addrLine = new AddressLine();
				addrLines.getAddressLines().add(addrLine);
				addrLine.setValue(check.getAddress1().trim());
				addrLine.setIndex(new BigInteger("1"));
			}
			if(hasValue(check.getAddress2())){
				AddressLine addrLine = new AddressLine();
				addrLines.getAddressLines().add(addrLine);
				addrLine.setValue(check.getAddress2().trim());
				addrLine.setIndex(new BigInteger("2"));
			}
			if(hasValue(check.getCity())){
				address.setCityName(check.getCity().trim());
			}
			if(hasValue(check.getState())){
				ObjectFactory objectFactory = new ObjectFactory();	
				address.setStateCode(objectFactory.createLocationTypeStateCode(check.getState().trim()));
			}
			if(hasValue(check.getZip())){
				address.setPostalCode((check.getZip().trim()));
			}
			if(hasValue(check.getPaymentType())){
				MedicalBehServiceLineType.PaymentDetails.PaymentDetail.PaymentMethod paymentMethod = new MedicalBehServiceLineType.PaymentDetails.PaymentDetail.PaymentMethod();
				paymentDetail.setPaymentMethod(paymentMethod);
				paymentMethod.setValue(check.getPaymentType());
				paymentMethod.setCode(check.getPaymentType());
				paymentMethod.setCodeSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
			}
			
		}
	}
	/**
	 * @param claim
	 * @param mBEHClaim
	 */
	private void addClaimRemarks(StgCl claim, MedicalBehClaimType  mBEHClaim){

		LOGGER.debug("Adding Claim Remarks");
		
		if(hasValue(claim.getPrcIcesRmkTxt())){
			Remarks remarksType = new Remarks();
			mBEHClaim.setRemarks(remarksType);
	
			RemarkType remarkType = new RemarkType();
			remarksType.getRemarks().add(remarkType);
			remarkType.setIndex(new BigInteger("1")) ;

			RemarkLineType remarkLineType = new RemarkLineType();
			remarkType.getLines().add(remarkLineType);
			remarkLineType.setLine(claim.getPrcIcesRmkTxt()) ;
			remarkLineType.setIndex(new BigInteger("1")) ;			
		}
	}
	
	/**
	 * Add remarks for service line
	 * @param stgClSvc
	 * @param service
	 */
	private void addServiceRemarks(StgClSvc stgClSvc, MedicalBehServiceLineType service){

		LOGGER.debug("Adding Service Remarks");		
		if(hasValue(stgClSvc.getPrcIcesRsultCd()) || hasValue(stgClSvc.getPrcSvcIcesRmkTxt())){			
			Remarks remarksType = new Remarks();
			service.setRemarks(remarksType);
			/*RemarkType remarkType = remarksType.addNewRemark();			
			//remarkType.addLine(stgClSvc.getPrcIcesRsultCd().trim());			
			RemarkLineType remarkLineType = remarkType.addNewLine();		
			remarkLineType.setIndex(new BigInteger("1")) ;
			remarkLineType.setLine(stgClSvc.getPrcIcesRsultCd().trim()) ;*/
			
			//added for pricer work
			if(hasValue(stgClSvc.getPrcIcesRsultCd())){
				RemarkType remarkType = new RemarkType();
				remarksType.getRemarks().add(remarkType);				
				remarkType.setType(ClaimConstants.ICES_RESULT_CODE);
				RemarkLineType remarkLineType = new RemarkLineType();
				remarkType.getLines().add(remarkLineType);				
				remarkLineType.setIndex(new BigInteger("1")) ;
				remarkLineType.setLine(stgClSvc.getPrcIcesRsultCd().trim()) ;
				}
				if(hasValue(stgClSvc.getPrcSvcIcesRmkTxt())){
				RemarkType remarkType = new RemarkType();
				remarksType.getRemarks().add(remarkType);						
				remarkType.setType(ClaimConstants.ICES_REMARK_TEXT);
				RemarkLineType remarkLineType = new RemarkLineType();
				remarkType.getLines().add(remarkLineType);					
				remarkLineType.setIndex(new BigInteger("1")) ;
				remarkLineType.setLine(stgClSvc.getPrcSvcIcesRmkTxt().trim()) ;
				}			
		}
	}
	
  /**
   *  Logic to derive the service line number
	* if ocsa_cl_suf_nbr = '00' then
	*	if mult_sys_cd = 'm', then
	*		appl_full_svc_nbr = ohi_cl_nbr + mult_sys_cd + hphc_perst_cl_svc_nbr + hphc_cl_svc_adj_cd
	*	else 
	*		appl_full_svc_nbr = ohi_cl_nbr + hphc_perst_cl_svc_nbr + hphc_cl_svc_adj_cd
	*	else 
	*	if mult_sys_cd = 'm', then
	*		appl_full_svc_nbr = ohi_cl_nbr + ocsa_cl_suf_nbr + mult_sys_cd + hphc_perst_cl_svc_nbr + hphc_cl_svc_adj_cd
	*	else 
	*	appl_full_svc_nbr = ohi_cl_nbr +ocsa_cl_suf_nbr + hphc_perst_cl_svc_nbr + hphc_cl_svc_adj_cd
	*
    * @param claimNumber
	* @param stgClSvc
	*  
	*/
	private String getServiceLineNumber(String claimNumber, StgClSvc stgClSvc){

		StringBuilder sb = new StringBuilder();
		
		sb.append(claimNumber.trim());
		if(hasValue(stgClSvc.getOcsaClSufNbr()) 
				&& !ClaimConstants.OCSA_SUFFIX_NBR.equals(stgClSvc.getOcsaClSufNbr().trim())){
			sb.append(stgClSvc.getOcsaClSufNbr().trim());
		}

		if(hasValue(stgClSvc.getMultSysCd()) 
				&& ClaimConstants.MULTISYSTEM_INDICATOR.equals(stgClSvc.getMultSysCd().trim())){
			sb.append(stgClSvc.getMultSysCd().trim());
		}
		if(hasValue(stgClSvc.getHphcPerstClSvcNbr())){
			sb.append(stgClSvc.getHphcPerstClSvcNbr().trim());
		}
		if(hasValue(stgClSvc.getHphcClSvcAdjCd())){
			sb.append(stgClSvc.getHphcClSvcAdjCd().trim());
		}
		
		return sb.toString();
	}
	
	/**
	 * @param claim
	 * @param mBEHClaim
	 * @param codeDecodeUtil
	 * 
	 * Method to concatnate service categories from service lines to return in summary response of search operation
	 */
	private void addServiceCategories(StgCl claim,
			MedicalBehClaimType mBEHClaim, CodeDecodeUtil codeDecodeUtil) {

		if(claim != null && claim.getServices() != null){

			Set<String> serviceCategories = new HashSet<String>();
			StringBuffer serviceCategoryCodes = new StringBuffer();
			StringBuffer serviceCategoryDesc = new StringBuffer();
			int count = 0;

			for (StgClSvc stgClSvc : claim.getServices()) {

				if(hasValue(stgClSvc.getOhiClSvcCatCd())){
					serviceCategories.add(stgClSvc.getOhiClSvcCatCd().trim());
				}
			}

			for(String serviceCategory : serviceCategories){
				count++;
				serviceCategoryCodes.append(serviceCategory);
				String svcCatDesc = codeDecodeUtil.decodeServiceCategory(serviceCategory,ClaimConstants.CLAIM_SOURCE.OHI);
				if(svcCatDesc != null){
					serviceCategoryDesc.append(svcCatDesc);
				}
				if(count != serviceCategories.size()){
					serviceCategoryCodes.append(",");
					serviceCategoryDesc.append(",");
				}	
			}

			if(hasValue(serviceCategoryCodes.toString())){
				MedicalBehClaimType.ServiceCategory serviceCategory = new MedicalBehClaimType.ServiceCategory();
				mBEHClaim.setServiceCategory(serviceCategory);
				serviceCategory.setCode(serviceCategoryCodes.toString());
				serviceCategory.setValue(serviceCategoryDesc.toString());
				serviceCategory.setCodeSource(ClaimConstants.CLAIM_SOURCE.OHI.toString());
			}
		}

	}

	/**
	 * Get Affiliation id from service line
	 * @param claim
	 * @return affiliation id
	 */
	private String getClaimProviderAffiliationId(StgCl claim) {

		String affiliationId = null;

		if(claim.getServices() != null && !claim.getServices().isEmpty() && hasValue(claim.getSvcPvNbr())){
			for(StgClSvc service : claim.getServices()){
				if(hasValue(service.getSvcPvNbr()) && claim.getSvcPvNbr().trim().equals(service.getSvcPvNbr().trim()) && hasValue(service.getSvcPvCntrtAffNbr())){
					affiliationId = service.getSvcPvCntrtAffNbr();
					break;
				}
			}
		}

		return affiliationId;
	}	
	
	/**
	 *  Populate ClaimDiagnosisRelGrpCd
	 * @param stgCl		
	 */
	private void addClaimDiagnosisRelGrpCd(StgCl claim,
			MedicalBehClaimType mBEHClaim, CodeDecodeUtil codeDecodeUtil) {
		if(hasValue(claim.getPrcDrgCd()) && hasValue(claim.getSubmDrgCd())){			
			DiagnosisRelatedGrps diagnosisRel = new DiagnosisRelatedGrps();
			mBEHClaim.setDiagnosisRelatedGrps(diagnosisRel);
			if(hasValue(claim.getPrcDrgCd()) && hasValue(claim.getPrcDrgVerNbr()) 
					&& hasValue(claim.getPrcDrgMthdCd()) && claim.getPrcDrgWtQty() != null){				
				DiagnosisRelatedGrp  diagnosisRltGrp	= new DiagnosisRelatedGrp();
				diagnosisRel.getDiagnosisRelatedGrps().add(diagnosisRltGrp);			
				MedicalBehClaimType.DiagnosisRelatedGrps.DiagnosisRelatedGrp.Code code = new MedicalBehClaimType.DiagnosisRelatedGrps.DiagnosisRelatedGrp.Code();
				diagnosisRltGrp.setCode(code);
				code.setCode(claim.getPrcDrgCd().trim());
								
				CodeSet conCode = codeDecodeUtil.getDecodedCode(Constants.CODE.DIAGNOSIS_RELATED_CD,claim.getPrcDrgCd().trim(),ClaimConstants.CLAIM_SOURCE.OHI);
				if(conCode != null && hasValue(conCode.getDescription())){
					code.setValue(conCode.getDescription());
				}	
				
				diagnosisRltGrp.setVersion(claim.getPrcDrgVerNbr().trim()); //setting version
				
				MedicalBehClaimType.DiagnosisRelatedGrps.DiagnosisRelatedGrp.MethodCode methodCode = new MedicalBehClaimType.DiagnosisRelatedGrps.DiagnosisRelatedGrp.MethodCode();
				diagnosisRltGrp.setMethodCode(methodCode);
				methodCode.setValue(claim.getPrcDrgMthdCd().trim());	
				
				diagnosisRltGrp.setWeightQuantity(claim.getPrcDrgWtQty().toBigInteger());		//setting wghtQty								
				diagnosisRltGrp.setLifeCycle(org.hphc.schema.claim.v6.LifeCycleEnumType.PRICED);
			}
			if(hasValue(claim.getSubmDrgCd())){				
				MedicalBehClaimType.DiagnosisRelatedGrps.DiagnosisRelatedGrp submittedCode = new MedicalBehClaimType.DiagnosisRelatedGrps.DiagnosisRelatedGrp();
				diagnosisRel.getDiagnosisRelatedGrps().add(submittedCode);
				
				MedicalBehClaimType.DiagnosisRelatedGrps.DiagnosisRelatedGrp.Code code = new MedicalBehClaimType.DiagnosisRelatedGrps.DiagnosisRelatedGrp.Code();
				submittedCode.setCode(code);
				code.setValue(claim.getSubmDrgCd().trim());			
				submittedCode.setLifeCycle(org.hphc.schema.claim.v6.LifeCycleEnumType.SUBMITTED);								
			}
			
		}
	}
	
	private void specialityCodeMedicalService(StgClSvc stgClSvc,
			ProviderType provider, CodeDecodeUtil codeDecodeUtil){
		if(hasValue(stgClSvc.getPrcSvcPvSpeclCd())
				|| hasValue(stgClSvc.getPrcSvcPvSpeclTyp2Cd())
				|| hasValue(stgClSvc.getPrcSvcPvSpeclTyp3Cd())
				|| hasValue(stgClSvc.getPrcSvcPvSpeclTyp4Cd())
				|| hasValue(stgClSvc.getPrcSvcPvSpeclTyp5Cd())
				|| hasValue(stgClSvc.getPrcSvcPvSpeclTyp6Cd())){
			if(hasValue(stgClSvc.getPrcSvcPvSpeclCd())){
				ProviderType.Speciality valueType = new ProviderType.Speciality();
				provider.getSpecialities().add(valueType);
				valueType.setCode(stgClSvc.getPrcSvcPvSpeclCd().trim());
				
				CodeSet specCode = codeDecodeUtil.getDecodedCode(Constants.CODE.PROVIDER_SPECIALTY,stgClSvc.getPrcSvcPvSpeclCd().trim(),ClaimConstants.CLAIM_SOURCE.OHI);
				if(specCode != null && hasValue(specCode.getDescription())){
					valueType.setValue(specCode.getDescription());
				}			
			}
			if(hasValue(stgClSvc.getPrcSvcPvSpeclTyp2Cd())){
				ProviderType.Speciality valueType = new ProviderType.Speciality();
				provider.getSpecialities().add(valueType);
				valueType.setCode(stgClSvc.getPrcSvcPvSpeclTyp2Cd().trim());				
				
				CodeSet specCode = codeDecodeUtil.getDecodedCode(Constants.CODE.PROVIDER_SPECIALTY,stgClSvc.getPrcSvcPvSpeclTyp2Cd().trim(),ClaimConstants.CLAIM_SOURCE.OHI);
				if(specCode != null && hasValue(specCode.getDescription())){
					valueType.setValue(specCode.getDescription());
				}
			}
			if(hasValue(stgClSvc.getPrcSvcPvSpeclTyp3Cd())){
				ProviderType.Speciality valueType = new ProviderType.Speciality();
				provider.getSpecialities().add(valueType);
				valueType.setCode(stgClSvc.getPrcSvcPvSpeclTyp3Cd().trim());	
				
				CodeSet specCode = codeDecodeUtil.getDecodedCode(Constants.CODE.PROVIDER_SPECIALTY,stgClSvc.getPrcSvcPvSpeclTyp3Cd().trim(),ClaimConstants.CLAIM_SOURCE.OHI);
				if(specCode != null && hasValue(specCode.getDescription())){
					valueType.setValue(specCode.getDescription());
				}
			}
			if(hasValue(stgClSvc.getPrcSvcPvSpeclTyp4Cd())){
				ProviderType.Speciality valueType = new ProviderType.Speciality();
				provider.getSpecialities().add(valueType);
				valueType.setCode(stgClSvc.getPrcSvcPvSpeclTyp4Cd().trim());	
				
				CodeSet specCode = codeDecodeUtil.getDecodedCode(Constants.CODE.PROVIDER_SPECIALTY,stgClSvc.getPrcSvcPvSpeclTyp4Cd().trim(),ClaimConstants.CLAIM_SOURCE.OHI);
				if(specCode != null && hasValue(specCode.getDescription())){
					valueType.setValue(specCode.getDescription());
				}	
			}
			if(hasValue(stgClSvc.getPrcSvcPvSpeclTyp5Cd())){
				ProviderType.Speciality valueType = new ProviderType.Speciality();
				provider.getSpecialities().add(valueType);
				valueType.setCode(stgClSvc.getPrcSvcPvSpeclTyp5Cd().trim());
				
				CodeSet specCode = codeDecodeUtil.getDecodedCode(Constants.CODE.PROVIDER_SPECIALTY,stgClSvc.getPrcSvcPvSpeclTyp5Cd().trim(),ClaimConstants.CLAIM_SOURCE.OHI);
				if(specCode != null && hasValue(specCode.getDescription())){
					valueType.setValue(specCode.getDescription());
				}
			}
			if(hasValue(stgClSvc.getPrcSvcPvSpeclTyp6Cd())){
				ProviderType.Speciality valueType = new ProviderType.Speciality();
				provider.getSpecialities().add(valueType);
				valueType.setCode(stgClSvc.getPrcSvcPvSpeclTyp6Cd().trim());	
				
				CodeSet specCode = codeDecodeUtil.getDecodedCode(Constants.CODE.PROVIDER_SPECIALTY,stgClSvc.getPrcSvcPvSpeclTyp6Cd().trim(),ClaimConstants.CLAIM_SOURCE.OHI);
				if(specCode != null && hasValue(specCode.getDescription())){
					valueType.setValue(specCode.getDescription());
				}
			}
		}
	}
	
	
	
	private   Map<Long, StgClCond> getConditionCode(List<StgCl> stgCls){
		List<StgClCond> claimCondCode = null ;	
		Map<Long,StgClCond>conditionCodeMap = null;
		QueryBudgetContext qbc = LoggingThreadContext.getQueryBudgetContext();		
		for (StgCl stgCl : stgCls){			
			long starttime = System.currentTimeMillis();
			claimCondCode = conditionCodeDao.getclaimConditionCode(stgCl);		
			qbc.setOhiServiceLinesAuditQueryTime((System.currentTimeMillis() - starttime));
		}
		
		if(claimCondCode != null ){
			for(StgClCond conditionCode : claimCondCode){
				conditionCodeMap = new HashMap<Long, StgClCond>();
				conditionCodeMap.put(conditionCode.getStgClId(), conditionCode);
			}
		}		
		return conditionCodeMap;
	}
			
	// Get External Editors
	private Map<String, StgClTpPrcssElgbl>getExternalEditor(List<StgCl> stgCls){		
		Set<Long> stgClIds = new HashSet<Long>();

		for (StgCl stgCl : stgCls){
			stgClIds.add(stgCl.getStgClId());
			
		}
		return getExternalEditorInfo(stgClIds);
	}
	
	private Map<String, StgClTpPrcssElgbl> getExternalEditorInfo(Set<Long>stgClIds){
		
		List<StgClTpPrcssElgbl> externalEditorsList = null ;
		Map<String,StgClTpPrcssElgbl>externalEditorMap = null;
		QueryBudgetContext qbc = LoggingThreadContext.getQueryBudgetContext();		
		long starttime = System.currentTimeMillis();
		externalEditorsList = stgClTpPrcssElgblDao.getExternalEditors(stgClIds);		
		qbc.setOhiServiceLinesAuditQueryTime((System.currentTimeMillis() - starttime));
		
		
		if(externalEditorsList != null ){
			externalEditorMap = new HashMap<String, StgClTpPrcssElgbl>();
			for(StgClTpPrcssElgbl externalEditor : externalEditorsList){				
				externalEditorMap.put(String.valueOf(externalEditor.getStgClId())+"-"+externalEditor.getVendorCode(), externalEditor);
			}
		}		
		return externalEditorMap;
	}
	
	/**
	 * Get NPI from service line
	 * @param claim
	 * @return NPI
	 */
	private String getClaimProviderNPI(StgCl claim) {

		String npi = null;

		if(claim.getServices() != null && !claim.getServices().isEmpty() && hasValue(claim.getSvcPvNbr())){
			for(StgClSvc service : claim.getServices()){
				if(hasValue(service.getSvcPvNbr()) && claim.getSvcPvNbr().trim().equals(service.getSvcPvNbr().trim()) && hasValue(service.getProviderNpi())){
					npi = service.getProviderNpi();
					break;
				}
			}
		}

		return npi;
	}
	
	void addExternalEditors(StgCl claim, MedicalBehClaimType mBEHClaim, CodeDecodeUtil codeDecodeUtil, Map<String,StgClTpPrcssElgbl>externalEditorMap){
		
		if(externalEditorMap != null && !externalEditorMap.keySet().isEmpty()){
			MedicalBehClaimType.ExternalEditors externalEditors = null;
			///need some modification
			Set<String> stgClId_VndrCd = new HashSet<String>();
			for(String stgClTpPrcessElgblKey : externalEditorMap.keySet()){
				stgClId_VndrCd.add(stgClTpPrcessElgblKey.split("-")[0]);
			}
			if(stgClId_VndrCd.contains(String.valueOf(claim.getStgClId()))){
					externalEditors = new MedicalBehClaimType.ExternalEditors();
					mBEHClaim.setExternalEditors(externalEditors);
			 }
						
			
			for(String stgClTpPrcessElgblKey : externalEditorMap.keySet()){
				String stgClId = stgClTpPrcessElgblKey.split("-")[0];
				if(stgClId.equals(String.valueOf(claim.getStgClId()))){
					StgClTpPrcssElgbl stgClTpPrcssElgbl = externalEditorMap.get(stgClTpPrcessElgblKey);
					ExternalEditorType externalEditorType =  new ExternalEditorType();
					
					if(hasValue(stgClTpPrcssElgbl.getVendorCode())){
						externalEditorType.setValue(stgClTpPrcssElgbl.getVendorCode());
						
						CodeSet vendorCode = codeDecodeUtil.getDecodedCode(Constants.CODE.VEDNOR_CD,stgClTpPrcssElgbl.getVendorCode().trim(),ClaimConstants.CLAIM_SOURCE.OHI);
						if(vendorCode != null && hasValue(vendorCode.getDescription())){
							externalEditorType.setVendorCodeDescription(vendorCode.getDescription());
						}
					}				
					
					if(stgClTpPrcssElgbl.getApplyClaimVerNbr() != null){
						externalEditorType.setApplyClaimVerNbr(new BigInteger(stgClTpPrcssElgbl.getApplyClaimVerNbr().toString().trim()));
					}
					
					if(hasValue(stgClTpPrcssElgbl.getStatCode())){
						externalEditorType.setEditorStatCode(stgClTpPrcssElgbl.getStatCode());
					}
					
					if(stgClTpPrcssElgbl.getEligibilityIndi() != null){
						externalEditorType.setEligibilityIndicator(stgClTpPrcssElgbl.getEligibilityIndi().toString());
					}
					
					if(stgClTpPrcssElgbl.getProtyNbr() != null){
						externalEditorType.setProtyNbr(new BigInteger(stgClTpPrcssElgbl.getProtyNbr().toString().trim()));
					}
					externalEditors.getExternaleditors().add(externalEditorType);
				}
				}
			  }
			}
		
		
		
		/*if(externalEditorMap != null && externalEditorMap.get(claim.getStgClId())!= null){
			StgClTpPrcssElgbl stgClTpPrcssElgbl = externalEditorMap.get(claim.getStgClId());
			
			
			for(StgClTpPrcssElgbl stgClTpPrcssElgbl : stgClTpPrcssElgblList){
				ExternalEditorType externalEditorType =  new ExternalEditorType();
				
				if(hasValue(stgClTpPrcssElgbl.getVendorCode())){
					externalEditorType.setValue(stgClTpPrcssElgbl.getVendorCode());
					
					CodeSet vendorCode = codeDecodeUtil.getDecodedCode(Constants.CODE.VEDNOR_CD,stgClTpPrcssElgbl.getVendorCode().trim(),ClaimConstants.CLAIM_SOURCE.OHI);
					if(vendorCode != null && hasValue(vendorCode.getDescription())){
						externalEditorType.setVendorCodeDescription(vendorCode.getDescription());
					}
				}				
				
				if(stgClTpPrcssElgbl.getApplyClaimVerNbr() != null){
					externalEditorType.setApplyClaimVerNbr(new BigInteger(stgClTpPrcssElgbl.getApplyClaimVerNbr().toString().trim()));
				}
				
				if(hasValue(stgClTpPrcssElgbl.getStatCode())){
					externalEditorType.setEditorStatCode(stgClTpPrcssElgbl.getStatCode());
				}
				
				if(stgClTpPrcssElgbl.getEligibilityIndi() != null){
					externalEditorType.setEligibilityIndicator(stgClTpPrcssElgbl.getEligibilityIndi().toString());
				}
				
				if(stgClTpPrcssElgbl.getProtyNbr() != null){
					externalEditorType.setProtyNbr(new BigInteger(stgClTpPrcssElgbl.getProtyNbr().toString().trim()));
				}
				externalEditors.getExternaleditors().add(externalEditorType);
			}
		}*/
		
		//externalEditorType.setVendorCode("vendorCOde");
	
	
	private  void addClaimConditionCode(StgCl claim, Map<Long,StgClCond> conditionCodeMap,	MedicalBehClaimType mBEHClaim, CodeDecodeUtil codeDecodeUtil){
		
		if(conditionCodeMap != null  && conditionCodeMap.get(claim.getStgClId())!= null){
			StgClCond conditionCode = conditionCodeMap.get(claim.getStgClId());
			
		if(conditionCode != null ){			
					if(hasValue(conditionCode.getCond1Cd()) || hasValue(conditionCode.getCond2Cd()) ||
							hasValue(conditionCode.getCond3Cd()) || hasValue(conditionCode.getCond4Cd()) ||
							hasValue(conditionCode.getCond5Cd()) || hasValue(conditionCode.getCond6Cd()) ||
							hasValue(conditionCode.getCond7Cd()) || hasValue(conditionCode.getCond8Cd()) ||
							hasValue(conditionCode.getCond9Cd()) || hasValue(conditionCode.getCond10Cd()) ||
							hasValue(conditionCode.getCond11Cd()) || hasValue(conditionCode.getCond12Cd()) ||
							hasValue(conditionCode.getCond13Cd()) || hasValue(conditionCode.getCond14Cd()) ||
							hasValue(conditionCode.getCond15Cd()) || hasValue(conditionCode.getCond16Cd()) ||
							hasValue(conditionCode.getCond17Cd()) || hasValue(conditionCode.getCond18Cd()) ||
							hasValue(conditionCode.getCond19Cd()) || hasValue(conditionCode.getCond20Cd()) ||
							hasValue(conditionCode.getCond21Cd()) || hasValue(conditionCode.getCond22Cd()) ||
							hasValue(conditionCode.getCond23Cd()) || hasValue(conditionCode.getCond24Cd())){
						
						
						ConditionCodes conditions  =  new ConditionCodes();
						mBEHClaim.setConditionCodes(conditions);
						if(hasValue(conditionCode.getCond1Cd())){							
							MedicalBehClaimType.ConditionCodes.ConditionCode code = new MedicalBehClaimType.ConditionCodes.ConditionCode();
							conditions.getConditionCodes().add(code);
							code.setCode(conditionCode.getCond1Cd());							
							code.setIndex(new BigInteger("1"));
							code.setType(ClaimConstants.HIPAA);						

							CodeSet conCode = codeDecodeUtil.getDecodedCode(Constants.CODE.CONDITION,conditionCode.getCond1Cd().trim(),ClaimConstants.CLAIM_SOURCE.OHI);
							if(conCode != null && hasValue(conCode.getDescription())){
								code.setValue(conCode.getDescription());
							}	
						}
						if(hasValue(conditionCode.getCond2Cd())){
							MedicalBehClaimType.ConditionCodes.ConditionCode code = new MedicalBehClaimType.ConditionCodes.ConditionCode();
							conditions.getConditionCodes().add(code);
							code.setCode(conditionCode.getCond2Cd());
							code.setIndex(new BigInteger("2"));
							code.setType(ClaimConstants.HIPAA);
							
							CodeSet conCode = codeDecodeUtil.getDecodedCode(Constants.CODE.CONDITION,conditionCode.getCond2Cd().trim(),ClaimConstants.CLAIM_SOURCE.OHI);
							if(conCode != null && hasValue(conCode.getDescription())){
								code.setValue(conCode.getDescription());
							}	
						}
						if(hasValue(conditionCode.getCond3Cd())){
							MedicalBehClaimType.ConditionCodes.ConditionCode code = new MedicalBehClaimType.ConditionCodes.ConditionCode();
							conditions.getConditionCodes().add(code);
							code.setCode(conditionCode.getCond3Cd());
							code.setIndex(new BigInteger("3"));
							code.setType(ClaimConstants.HIPAA);
							
							CodeSet conCode = codeDecodeUtil.getDecodedCode(Constants.CODE.CONDITION,conditionCode.getCond3Cd().trim(),ClaimConstants.CLAIM_SOURCE.OHI);
							if(conCode != null && hasValue(conCode.getDescription())){
								code.setValue(conCode.getDescription());
							}	
						}
						if(hasValue(conditionCode.getCond4Cd())){
							MedicalBehClaimType.ConditionCodes.ConditionCode code = new MedicalBehClaimType.ConditionCodes.ConditionCode();
							conditions.getConditionCodes().add(code);
							code.setCode(conditionCode.getCond4Cd());
							code.setIndex(new BigInteger("4"));
							code.setType(ClaimConstants.HIPAA);
							
							CodeSet conCode = codeDecodeUtil.getDecodedCode(Constants.CODE.CONDITION,conditionCode.getCond4Cd().trim(),ClaimConstants.CLAIM_SOURCE.OHI);
							if(conCode != null && hasValue(conCode.getDescription())){
								code.setValue(conCode.getDescription());
							}	
						}
						if(hasValue(conditionCode.getCond5Cd())){
							MedicalBehClaimType.ConditionCodes.ConditionCode code = new MedicalBehClaimType.ConditionCodes.ConditionCode();
							conditions.getConditionCodes().add(code);
							code.setCode(conditionCode.getCond5Cd());
							code.setIndex(new BigInteger("5"));
							code.setType(ClaimConstants.HIPAA);
							
							CodeSet conCode = codeDecodeUtil.getDecodedCode(Constants.CODE.CONDITION,conditionCode.getCond5Cd().trim(),ClaimConstants.CLAIM_SOURCE.OHI);
							if(conCode != null && hasValue(conCode.getDescription())){
								code.setValue(conCode.getDescription());
							}	
						}
						if(hasValue(conditionCode.getCond6Cd())){
							MedicalBehClaimType.ConditionCodes.ConditionCode code = new MedicalBehClaimType.ConditionCodes.ConditionCode();
							conditions.getConditionCodes().add(code);
							code.setCode(conditionCode.getCond6Cd());
							code.setIndex(new BigInteger("6"));
							code.setType(ClaimConstants.HIPAA);
							
							CodeSet conCode = codeDecodeUtil.getDecodedCode(Constants.CODE.CONDITION,conditionCode.getCond6Cd().trim(),ClaimConstants.CLAIM_SOURCE.OHI);
							if(conCode != null && hasValue(conCode.getDescription())){
								code.setValue(conCode.getDescription());
							}	
						}
						if(hasValue(conditionCode.getCond7Cd())){
							MedicalBehClaimType.ConditionCodes.ConditionCode code = new MedicalBehClaimType.ConditionCodes.ConditionCode();
							conditions.getConditionCodes().add(code);
							code.setCode(conditionCode.getCond7Cd());
							code.setIndex(new BigInteger("7"));
							code.setType(ClaimConstants.HIPAA);
							
							CodeSet conCode = codeDecodeUtil.getDecodedCode(Constants.CODE.CONDITION,conditionCode.getCond7Cd().trim(),ClaimConstants.CLAIM_SOURCE.OHI);
							if(conCode != null && hasValue(conCode.getDescription())){
								code.setValue(conCode.getDescription());
							}	
						}
						if(hasValue(conditionCode.getCond8Cd())){
							MedicalBehClaimType.ConditionCodes.ConditionCode code = new MedicalBehClaimType.ConditionCodes.ConditionCode();
							conditions.getConditionCodes().add(code);
							code.setCode(conditionCode.getCond8Cd());
							code.setIndex(new BigInteger("8"));
							code.setType(ClaimConstants.HIPAA);
							
							CodeSet conCode = codeDecodeUtil.getDecodedCode(Constants.CODE.CONDITION,conditionCode.getCond8Cd().trim(),ClaimConstants.CLAIM_SOURCE.OHI);
							if(conCode != null && hasValue(conCode.getDescription())){
								code.setValue(conCode.getDescription());
							}	
						}
						if(hasValue(conditionCode.getCond9Cd())){
							MedicalBehClaimType.ConditionCodes.ConditionCode code = new MedicalBehClaimType.ConditionCodes.ConditionCode();
							conditions.getConditionCodes().add(code);
							code.setCode(conditionCode.getCond9Cd());
							code.setIndex(new BigInteger("9"));
							code.setType(ClaimConstants.HIPAA);
							
							CodeSet conCode = codeDecodeUtil.getDecodedCode(Constants.CODE.CONDITION,conditionCode.getCond9Cd().trim(),ClaimConstants.CLAIM_SOURCE.OHI);
							if(conCode != null && hasValue(conCode.getDescription())){
								code.setValue(conCode.getDescription());
							}	
				     	}
						if(hasValue(conditionCode.getCond10Cd())){
							MedicalBehClaimType.ConditionCodes.ConditionCode code = new MedicalBehClaimType.ConditionCodes.ConditionCode();
							conditions.getConditionCodes().add(code);
							code.setCode(conditionCode.getCond10Cd());
							code.setIndex(new BigInteger("10"));
							code.setType(ClaimConstants.HIPAA);
							
							CodeSet conCode = codeDecodeUtil.getDecodedCode(Constants.CODE.CONDITION,conditionCode.getCond10Cd().trim(),ClaimConstants.CLAIM_SOURCE.OHI);
							if(conCode != null && hasValue(conCode.getDescription())){
								code.setValue(conCode.getDescription());
							}	
						}
						if(hasValue(conditionCode.getCond11Cd())){;
							MedicalBehClaimType.ConditionCodes.ConditionCode code = new MedicalBehClaimType.ConditionCodes.ConditionCode();
							conditions.getConditionCodes().add(code);
							code.setCode(conditionCode.getCond11Cd());
							code.setIndex(new BigInteger("11"));
							code.setType(ClaimConstants.HIPAA);
							
							CodeSet conCode = codeDecodeUtil.getDecodedCode(Constants.CODE.CONDITION,conditionCode.getCond11Cd().trim(),ClaimConstants.CLAIM_SOURCE.OHI);
							if(conCode != null && hasValue(conCode.getDescription())){
								code.setValue(conCode.getDescription());
							}	
						}
						if(hasValue(conditionCode.getCond12Cd())){
							MedicalBehClaimType.ConditionCodes.ConditionCode code = new MedicalBehClaimType.ConditionCodes.ConditionCode();
							conditions.getConditionCodes().add(code);
							code.setCode(conditionCode.getCond12Cd());
							code.setIndex(new BigInteger("12"));
							code.setType(ClaimConstants.HIPAA);
							
							CodeSet conCode = codeDecodeUtil.getDecodedCode(Constants.CODE.CONDITION,conditionCode.getCond12Cd().trim(),ClaimConstants.CLAIM_SOURCE.OHI);
							if(conCode != null && hasValue(conCode.getDescription())){
								code.setValue(conCode.getDescription());
							}	
						}
						if(hasValue(conditionCode.getCond13Cd())){
							MedicalBehClaimType.ConditionCodes.ConditionCode code = new MedicalBehClaimType.ConditionCodes.ConditionCode();
							conditions.getConditionCodes().add(code);
							code.setCode(conditionCode.getCond13Cd());
							code.setIndex(new BigInteger("13"));
							code.setType(ClaimConstants.HIPAA);
							
							CodeSet conCode = codeDecodeUtil.getDecodedCode(Constants.CODE.CONDITION,conditionCode.getCond13Cd().trim(),ClaimConstants.CLAIM_SOURCE.OHI);
							if(conCode != null && hasValue(conCode.getDescription())){
								code.setValue(conCode.getDescription());
							}	
						}
						if(hasValue(conditionCode.getCond14Cd())){
							MedicalBehClaimType.ConditionCodes.ConditionCode code = new MedicalBehClaimType.ConditionCodes.ConditionCode();
							conditions.getConditionCodes().add(code);
							code.setCode(conditionCode.getCond14Cd());
							code.setIndex(new BigInteger("14"));
							code.setType(ClaimConstants.HIPAA);
							
							CodeSet conCode = codeDecodeUtil.getDecodedCode(Constants.CODE.CONDITION,conditionCode.getCond14Cd().trim(),ClaimConstants.CLAIM_SOURCE.OHI);
							if(conCode != null && hasValue(conCode.getDescription())){
								code.setValue(conCode.getDescription());
							}	
							}
						if(hasValue(conditionCode.getCond15Cd())){
							MedicalBehClaimType.ConditionCodes.ConditionCode code = new MedicalBehClaimType.ConditionCodes.ConditionCode();
							conditions.getConditionCodes().add(code);
							code.setCode(conditionCode.getCond15Cd());
							code.setIndex(new BigInteger("15"));
							code.setType(ClaimConstants.HIPAA);
							
							CodeSet conCode = codeDecodeUtil.getDecodedCode(Constants.CODE.CONDITION,conditionCode.getCond15Cd().trim(),ClaimConstants.CLAIM_SOURCE.OHI);
							if(conCode != null && hasValue(conCode.getDescription())){
								code.setValue(conCode.getDescription());
							}	
						}
						if(hasValue(conditionCode.getCond16Cd())){
							MedicalBehClaimType.ConditionCodes.ConditionCode code = new MedicalBehClaimType.ConditionCodes.ConditionCode();
							conditions.getConditionCodes().add(code);
							code.setCode(conditionCode.getCond16Cd());
							code.setIndex(new BigInteger("16"));
							code.setType(ClaimConstants.HIPAA);
							
							CodeSet conCode = codeDecodeUtil.getDecodedCode(Constants.CODE.CONDITION,conditionCode.getCond16Cd().trim(),ClaimConstants.CLAIM_SOURCE.OHI);
							if(conCode != null && hasValue(conCode.getDescription())){
								code.setValue(conCode.getDescription());
							}	
						}
						if(hasValue(conditionCode.getCond17Cd())){
							MedicalBehClaimType.ConditionCodes.ConditionCode code = new MedicalBehClaimType.ConditionCodes.ConditionCode();
							conditions.getConditionCodes().add(code);
							code.setCode(conditionCode.getCond17Cd());
							code.setIndex(new BigInteger("17"));
							code.setType(ClaimConstants.HIPAA);
							
							CodeSet conCode = codeDecodeUtil.getDecodedCode(Constants.CODE.CONDITION,conditionCode.getCond17Cd().trim(),ClaimConstants.CLAIM_SOURCE.OHI);
							if(conCode != null && hasValue(conCode.getDescription())){
								code.setValue(conCode.getDescription());
							}	
						}
						if(hasValue(conditionCode.getCond18Cd())){
							MedicalBehClaimType.ConditionCodes.ConditionCode code = new MedicalBehClaimType.ConditionCodes.ConditionCode();
							conditions.getConditionCodes().add(code);
							code.setCode(conditionCode.getCond18Cd());
							code.setIndex(new BigInteger("18"));
							code.setType(ClaimConstants.HIPAA);
							
							CodeSet conCode = codeDecodeUtil.getDecodedCode(Constants.CODE.CONDITION,conditionCode.getCond18Cd().trim(),ClaimConstants.CLAIM_SOURCE.OHI);
							if(conCode != null && hasValue(conCode.getDescription())){
								code.setValue(conCode.getDescription());
							}	
						}
						if(hasValue(conditionCode.getCond19Cd())){
							MedicalBehClaimType.ConditionCodes.ConditionCode code = new MedicalBehClaimType.ConditionCodes.ConditionCode();
							conditions.getConditionCodes().add(code);
							code.setCode(conditionCode.getCond19Cd());
							code.setIndex(new BigInteger("19"));
							code.setType(ClaimConstants.HIPAA);
							
							CodeSet conCode = codeDecodeUtil.getDecodedCode(Constants.CODE.CONDITION,conditionCode.getCond19Cd().trim(),ClaimConstants.CLAIM_SOURCE.OHI);
							if(conCode != null && hasValue(conCode.getDescription())){
								code.setValue(conCode.getDescription());
							}	
						}
						if(hasValue(conditionCode.getCond20Cd())){
							MedicalBehClaimType.ConditionCodes.ConditionCode code = new MedicalBehClaimType.ConditionCodes.ConditionCode();
							conditions.getConditionCodes().add(code);
							code.setCode(conditionCode.getCond20Cd());
							code.setIndex(new BigInteger("20"));
							code.setType(ClaimConstants.HIPAA);
							
							CodeSet conCode = codeDecodeUtil.getDecodedCode(Constants.CODE.CONDITION,conditionCode.getCond20Cd().trim(),ClaimConstants.CLAIM_SOURCE.OHI);
							if(conCode != null && hasValue(conCode.getDescription())){
								code.setValue(conCode.getDescription());
							}	
						}
						if(hasValue(conditionCode.getCond21Cd())){
							MedicalBehClaimType.ConditionCodes.ConditionCode code = new MedicalBehClaimType.ConditionCodes.ConditionCode();
							conditions.getConditionCodes().add(code);
							code.setCode(conditionCode.getCond21Cd());
							code.setIndex(new BigInteger("21"));
							code.setType(ClaimConstants.HIPAA);
							
							CodeSet conCode = codeDecodeUtil.getDecodedCode(Constants.CODE.CONDITION,conditionCode.getCond21Cd().trim(),ClaimConstants.CLAIM_SOURCE.OHI);
							if(conCode != null && hasValue(conCode.getDescription())){
								code.setValue(conCode.getDescription());
							}	
						}
						if(hasValue(conditionCode.getCond22Cd())){
							MedicalBehClaimType.ConditionCodes.ConditionCode code = new MedicalBehClaimType.ConditionCodes.ConditionCode();
							conditions.getConditionCodes().add(code);
							code.setCode(conditionCode.getCond22Cd());
							code.setIndex(new BigInteger("22"));
							code.setType(ClaimConstants.HIPAA);
							
							CodeSet conCode = codeDecodeUtil.getDecodedCode(Constants.CODE.CONDITION,conditionCode.getCond22Cd().trim(),ClaimConstants.CLAIM_SOURCE.OHI);
							if(conCode != null && hasValue(conCode.getDescription())){
								code.setValue(conCode.getDescription());
							}	
						}
						if(hasValue(conditionCode.getCond23Cd())){
							MedicalBehClaimType.ConditionCodes.ConditionCode code = new MedicalBehClaimType.ConditionCodes.ConditionCode();
							conditions.getConditionCodes().add(code);
							code.setCode(conditionCode.getCond23Cd());
							code.setIndex(new BigInteger("23"));
							code.setType(ClaimConstants.HIPAA);
							CodeSet conCode = codeDecodeUtil.getDecodedCode(Constants.CODE.CONDITION,conditionCode.getCond23Cd().trim(),ClaimConstants.CLAIM_SOURCE.OHI);
							if(conCode != null && hasValue(conCode.getDescription())){
								code.setValue(conCode.getDescription());
							}	
						}
						if(hasValue(conditionCode.getCond24Cd())){
							MedicalBehClaimType.ConditionCodes.ConditionCode code = new MedicalBehClaimType.ConditionCodes.ConditionCode();
							conditions.getConditionCodes().add(code);
							code.setCode(conditionCode.getCond24Cd());
							code.setIndex(new BigInteger("24"));
							code.setType(ClaimConstants.HIPAA);
							
							CodeSet conCode = codeDecodeUtil.getDecodedCode(Constants.CODE.CONDITION,conditionCode.getCond24Cd().trim(),ClaimConstants.CLAIM_SOURCE.OHI);
							if(conCode != null && hasValue(conCode.getDescription())){
								code.setValue(conCode.getDescription());
							}	
					  }
					}
			}
							
		}
	}
			
}
