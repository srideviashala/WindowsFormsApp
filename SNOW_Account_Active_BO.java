/*
 * SNOW_Account_Active_BO.java
 *
 * Created on 2017-10-05
 * for Project: Garancy Identity Manager
 * by cxwe
 *
 * (C) 2003-2018 by Beta Systems IAM Software AG
 */
package com.betasystems.sam.snow.custJava;

import java.util.Vector;

import com.betasystems.sam.server.IF_BO;
import com.betasystems.sam.server.IF_ServerUtility;
import com.betasystems.sam.server.ServerUtilityFactory;
import com.betasystems.sam.shared.IF_InternalProperties;
import com.betasystems.sam.shared.SamServerException;
import com.betasystems.sam.shared.UnknownAttributeException;
import com.betasystems.sam.snow.genJava.IF_SNOW_Account_Active_BO;
import com.betasystems.sam.util.PropertyBundle;

/**
 * This class contains the Business Server logic for ServiceNow Accounts.
 *
 * @author cxwe
 *
 */
public class SNOW_Account_Active_BO extends com.betasystems.sam.snow.samJava.SNOW_Account_Active_BO implements IF_SNOW_Account_Active_BO
{

   /**
    * Symbolic name of the 1:1 child Address Data.
    */
   private static final String SYMB_NAME_ORG_DATA = "ORGDATA";

   /**
    * Symbolic name of the 1:1 child Address Data.
    */
   private static final String SYMB_NAME_ACCOUNT  = "ROOT";

   /**
    * Shows whether the automatic OBOXUSS entries are already inserted.
    */
   private boolean             alreadyInserted;


   /**
    * Constructor.
    *
    * @param params
    *           creation parameters
    * @throws SamServerException
    *            in case of errors
    */
   public SNOW_Account_Active_BO(Vector params) throws SamServerException
   {
      super(params);
   }


   /**
    * Alternative constructor.
    *
    * @param theKeys
    *           creation parameters
    * @param dummy
    *           for signature only
    * @throws SamServerException
    *            in case of errors
    */
   public SNOW_Account_Active_BO(Vector theKeys, String dummy) throws SamServerException
   {
      super(theKeys, dummy);
   }


   /** {@inheritDoc} */
   @Override
   final public boolean exitCustOpenBO() throws Exception
   {

      hideUnhideDomainFields(OBOXUS_C_01_09L);

      return super.exitCustOpenBO();
   }


   /*
    * (non-Javadoc)
    *
    * @see com.betasystems.sam.server.BOCore#exitCustCreationDialogFinished()
    */
   @Override
   public boolean exitCustCreationDialogFinished() throws Exception
   {

      hideUnhideDomainFields(OBOXUS_C_01_09L);

      // TODO Auto-generated method stub
      return super.exitCustCreationDialogFinished();
   }


   @Override
   public boolean exitCustChangeBO(Vector subsequentSyncs) throws Exception
   {
      /*
       * ------------------------------------------------------------------------------ Insert 1:1 sons ------------------------------------------------------------------------------
       */
      if (!alreadyInserted && !getFatherBO().wasCopied() && getStatus() == INSERTED)
      {
         IF_ServerUtility util = ServerUtilityFactory.create(this);

         IF_BO addressData = util.insertChildBO(this, SYMB_NAME_ORG_DATA);
         if (addressData != null)
         {
            addressData.setValue(OBOXUS_TKID, getValue(OBOXUS_TKID));
         }

         alreadyInserted = true;
      }

      return super.exitCustChangeBO(subsequentSyncs);
   }


   /**
    * get meaning for valid value.
    *
    * @param aBO
    *           the BO
    * @param aAttribute
    *           the attribute
    * @return the value meaning.
    * @throws SamServerException
    *            in case of errors
    */
   static String getMeaning(IF_BO aBO, String aAttribute) throws SamServerException
   {
      String aValue = aBO.getValue(aAttribute);
      String aMeaning = PropertyBundle.getPropertyBundle(aBO.getBODesc().getValidValueMeaningsBaseNames()).getProperty(aAttribute + "." + aValue, aBO.getShortBOName(), false);
      if (null == aMeaning)
      {
         aMeaning = aValue;
      }
      return aMeaning;
   }


   /*
    * (non-Javadoc)
    *
    * @see com.betasystems.sam.server.BOCore#setValue(java.lang.String, java.lang.String)
    */
   @Override
   public void setValue(String aFieldName, String aValue)
   {
      if (aFieldName.equalsIgnoreCase("OBOXUS_C_01_09L"))
      {

         try
         {
            hideUnhideDomainFields(aFieldName);
         }
         catch (UnknownAttributeException e)
         {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
      }

      // TODO Auto-generated method stub
      super.setValue(aFieldName, aValue);
   }


   /**
    * @param aFieldName
    *           - name of the field for which the value is being set
    * @throws UnknownAttributeException
    *            - in case of errors
    */
   public void hideUnhideDomainFields(String aFieldName) throws UnknownAttributeException
   {
      if (getValue(aFieldName).equalsIgnoreCase("Y"))
      {
         removeInternalPropertyFor("OBOXUS_C_256_01L", IF_InternalProperties.INTPROP_HIDDEN_INT);
         removeInternalPropertyFor("OBOXUS_C_256_02L", IF_InternalProperties.INTPROP_HIDDEN_INT);
      }
      else if (getValue(aFieldName).equalsIgnoreCase("N"))
      {
         addInternalPropertyFor("OBOXUS_C_256_01L", IF_InternalProperties.INTPROP_HIDDEN_INT);
         addInternalPropertyFor("OBOXUS_C_256_02L", IF_InternalProperties.INTPROP_HIDDEN_INT);
      }
   }

}
