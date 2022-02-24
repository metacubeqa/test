package utility;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.openqa.selenium.json.Json;
import org.testng.Assert;
import org.testng.util.Strings;

import base.TestBase;
import guidedSelling.base.GuidedSellingBase;
import guidedSelling.source.pageClasses.SequencesPage.CriteriaFields;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SalesForceAPIUtility extends GuidedSellingBase {
//public static Properties CONFIG = new Properties();

	
	static OkHttpClient client;
	static MediaType mediaType;
	static RequestBody body;
	static Request request;
	static Response response ;
	static String jsonData = null;
	static Document doc = null;
	
	
	public static enum updateFields{
		AnnualRevenue,
		Title,
		City,
		Email,
		Website,
		Fax,
		Industry,
		HasOptedOutOfFax,
		
		AnnualRevenuevalue,
		Titlevalue,
		Cityvalue,
		Emailvalue,
		Websitevalue,
		Faxvalue,
		Industryvalue,
		HasOptedOutOfFaxvalue,
		
		
	}
	
	/**
	 * @param token
	 * @param url
	 * @param firstName
	 * @param lastName
	 */
	public static String createContact(String token, String url, String firstName, String lastName) {
		String contactId = null;
		 String accountId = CONFIG.getProperty("account_id");
		try {
			client = new OkHttpClient().newBuilder().connectTimeout(500, TimeUnit.SECONDS)
				    .writeTimeout(50, TimeUnit.SECONDS)
				    .readTimeout(300, TimeUnit.SECONDS)
				    .build();
			mediaType = MediaType.parse("application/json");
			
			 body = RequestBody.create(mediaType, "{\n\"FirstName\" : \""+firstName+"\",\n\"LastName\":\""+lastName+"\","
					+ "\n\"AccountId\": \""+accountId+"\",\n\"Email\" : \"ringdna.testing@metacube.com\",\n\"Phone\" : \"+12054833130\",\n\"Status__c\" : \"Open\","
					+ "\n\"LeadSource\": \"Channel\"\n}\n");
			request = new Request.Builder()
					.url(url+"services/data/v50.0/sobjects/Contact")
					.method("POST", body).addHeader("Authorization", "Bearer " + token)
					.addHeader("Content-Type", "application/json").build();

			   // Read from request
			response = client.newCall(request).execute();
			String jsonData = response.body().string();
		    JSONObject Jobject = new JSONObject(jsonData);
		    contactId  = Jobject.get("id").toString();

		    assertTrue(Strings.isNotNullAndNotEmpty(contactId));
			assertEquals(response.message(), "Created");
			assertEquals(response.code(), 201);
			
		} catch (Exception e) {
			System.out.println("API error" + e.getMessage());
			Assert.fail();
		}
		return contactId;
	}
	
	/**
	 * @param url
	 * @param bearerToken
	 * @param contactId
	 */
	public static void deleteContact(String url, String bearerToken, String contactId) {
		try {
			client = new OkHttpClient().newBuilder().connectTimeout(10, TimeUnit.SECONDS)
				    .writeTimeout(10, TimeUnit.SECONDS)
				    .readTimeout(30, TimeUnit.SECONDS)
				    .build();
			mediaType = MediaType.parse("application/json");

			body = RequestBody.create(mediaType, "");
			
			request = new Request.Builder()
					.url(url+"/services/data/v50.0/sobjects/Contact/" + contactId)
					.method("DELETE", body).addHeader("Authorization", "Bearer " + bearerToken)
					.addHeader("Content-Type", "application/json").build();

			   // Read from request
			response = client.newCall(request).execute();
			
			assertEquals(response.message(), "No Content");
			assertEquals(response.code(), 204);
			
		} catch (Exception e) {
			System.out.println("API error" + e.getMessage());
			Assert.fail();
		}
	}

	/**
	 * @param url
	 * @param bearerToken
	 * @param contactId
	 * @param body
	 */
	public static void updateContact(String url, String bearerToken, String contactId, RequestBody body) {
		try {
			client = new OkHttpClient().newBuilder().connectTimeout(10, TimeUnit.SECONDS)
				    .writeTimeout(10, TimeUnit.SECONDS)
				    .readTimeout(30, TimeUnit.SECONDS)
				    .build();
			mediaType = MediaType.parse("application/json");

			request = new Request.Builder()
					.url(url+"/services/data/v50.0/sobjects/Contact/" + contactId)
					.method("PATCH", body).addHeader("Authorization", "Bearer " + bearerToken)
					.addHeader("Content-Type", "application/json").build();

			   // Read from request
			response = client.newCall(request).execute();
		    
			assertEquals(response.message(), "No Content");
			assertEquals(response.code(), 204);
			
			Thread.sleep(5*1000);
			
		} catch (Exception e) {
			System.out.println("API error" + e.getMessage());
			Assert.fail();
		}
	}
	
	/**
	 * @param token
	 * @param url
	 * @param firstName
	 * @param lastName
	 * @param company
	 * @return
	 */
	public static String createLead(String token, String url, String firstName, String lastName, String company ) {
		String ownerId = TestBase.CONFIG.getProperty("qa_user_salesforce_id");
		String leadId = null;
		try {
			client = new OkHttpClient().newBuilder().connectTimeout(10, TimeUnit.SECONDS)
				    .writeTimeout(10, TimeUnit.SECONDS)
				    .readTimeout(30, TimeUnit.SECONDS)
				    .build();
			mediaType = MediaType.parse("application/json"); 
	
			body = RequestBody.create(mediaType, "{\n\"FirstName\":\""+firstName+"\","
					+ "\n\"LastName\":\""+lastName+"\","
					+ "\n\"Company\": \"borders_content_based_invoice.spl\","
					+ "\n\"sdr_owner__c\": \""+ownerId+"\","
					+ "\n\"custom_lookup1__c\": \""+ownerId+"\","
					+ "\n\"LeadSource\": \"ABM\",\n\"Phone\": \"+12054833130\","
					+ "\n\"Email\": \"ringdna.testing@metacube.com\"\n\n\n}\n");
			
		
	
			request = new Request.Builder().url(url + "services/data/v50.0/sobjects/Lead")
					.method("POST", body).header("Authorization", "Bearer " + token)
					.header("Content-Type", "application/json").build();

			  // Read from request
			response = client.newCall(request).execute();
			String jsonData = response.body().string();
		    JSONObject Jobject = new JSONObject(jsonData);
		    leadId  = Jobject.get("id").toString();

		    assertTrue(Strings.isNotNullAndNotEmpty(leadId));
			assertEquals(response.message(), "Created");
			assertEquals(response.code(), 201);
			
		} catch (Exception e) {
			System.out.println("API error:-" + e.getMessage());
			Assert.fail();
		}
		return leadId;
	}

	/**
	 * @param url
	 * @param bearerToken
	 * @param leadId
	 */
	public static void deleteLead(String url, String bearerToken, String leadId) {
		try {
			client = new OkHttpClient().newBuilder().connectTimeout(10, TimeUnit.SECONDS)
				    .writeTimeout(10, TimeUnit.SECONDS)
				    .readTimeout(30, TimeUnit.SECONDS)
				    .build();
			mediaType = MediaType.parse("application/json");

			body = RequestBody.create(mediaType, "");
			
			request = new Request.Builder()
					.url(url+"/services/data/v50.0/sobjects/Lead/" + leadId)
					.method("DELETE", body).addHeader("Authorization", "Bearer " + bearerToken)
					.addHeader("Content-Type", "application/json").build();

			   // Read from request
			response = client.newCall(request).execute();
			
			assertEquals(response.message(), "No Content");
			assertEquals(response.code(), 204);
			
		} catch (Exception e) {
			System.out.println("API error" + e.getMessage());
			Assert.fail();
		}
	}
	
	/**
	 * @param url
	 * @param bearerToken
	 * @param leadId
	 * @param body
	 */
	public static void updateLead(String url, String bearerToken, String leadId, RequestBody body) {
		try {
			client = new OkHttpClient().newBuilder().connectTimeout(10, TimeUnit.SECONDS)
				    .writeTimeout(10, TimeUnit.SECONDS)
				    .readTimeout(30, TimeUnit.SECONDS)
				    .build();
			mediaType = MediaType.parse("application/json");

			request = new Request.Builder()
					.url(url+"/services/data/v50.0/sobjects/Lead/" + leadId)
					.method("PATCH", body).addHeader("Authorization", "Bearer " + bearerToken)
					.addHeader("Content-Type", "application/json").build();

			   // Read from request
			response = client.newCall(request).execute();
		    
			assertEquals(response.message(), "No Content");
			assertEquals(response.code(), 204);
			
			Thread.sleep(5*1000);
			
		} catch (Exception e) {
			System.out.println("API error" + e.getMessage());
			Assert.fail();
		}
	}
	
	
	/**
	 * @param token
	 * @param url
	 * @param opportunityName
	 * @param stageName
	 * @param accountId
	 * @param closedate
	 * @param contactId
	 * @return
	 */
	public static String createOpportunity(String token, String url,String StageName, String opportunityName, String accountId, String closedate, String contactId ) {
		String opportunityId = null;
		try {
			client = new OkHttpClient().newBuilder().connectTimeout(20, TimeUnit.SECONDS)
				    .writeTimeout(40, TimeUnit.SECONDS)
				    .readTimeout(50, TimeUnit.SECONDS)
				    .build();
			mediaType = MediaType.parse("application/json");
			//mediaType = MediaType.parse("application/json");
			RequestBody body = RequestBody.create(mediaType, "{\n\"Name\":\""+opportunityName+"\","
					+ "\n\"AccountId\": \""+accountId+"\","
					+ "\n\"StageName\": \"Prospecting\",\n\"CloseDate\": \"2025-03-23\"\n}\n");
			request = new Request.Builder()
					.url(url+"services/data/v48.0/sobjects/Opportunity")
					.method("POST", body).addHeader("Authorization", "Bearer " + token)
					.addHeader("Content-Type", "application/json").build();

			   // Read from request
			response = client.newCall(request).execute();
			String jsonData = response.body().string();
		    JSONObject Jobject = new JSONObject(jsonData);
		    opportunityId  = Jobject.get("id").toString();

		    assertTrue(Strings.isNotNullAndNotEmpty(opportunityId));
			assertEquals(response.message(), "Created");
			assertEquals(response.code(), 201);
			
		} catch (Exception e) {
			System.out.println("API error" + e.getMessage());
			Assert.fail();
		}
		return opportunityId;
	}

	/**
	 * @param url
	 * @param bearerToken
	 * @param opportunityId
	 * @param body
	 */
	public static void updateOpportunity(String url, String bearerToken, String opportunityId, RequestBody body) {
		try {
			client = new OkHttpClient().newBuilder().connectTimeout(10, TimeUnit.SECONDS)
				    .writeTimeout(10, TimeUnit.SECONDS)
				    .readTimeout(30, TimeUnit.SECONDS)
				    .build();
			mediaType = MediaType.parse("application/json");

			request = new Request.Builder()
					.url(url+"/services/data/v50.0/sobjects/Opportunity/"+opportunityId)
					.method("PATCH", body).addHeader("Authorization", "Bearer " + bearerToken)
					.addHeader("Content-Type", "application/json").build();

			   // Read from request
			response = client.newCall(request).execute();
		    
			assertEquals(response.message(), "No Content");
			assertEquals(response.code(), 204);
			
			Thread.sleep(5*1000);
			
		} catch (Exception e) {
			System.out.println("API error" + e.getMessage());
		}
	}

	/**
	 * @param url
	 * @param bearerToken
	 * @param opportunityId
	 */
	public static void deleteOpportunity(String url, String bearerToken, String opportunityId) {
		try {
			client = new OkHttpClient().newBuilder().connectTimeout(10, TimeUnit.SECONDS)
				    .writeTimeout(10, TimeUnit.SECONDS)
				    .readTimeout(30, TimeUnit.SECONDS)
				    .build();
			mediaType = MediaType.parse("application/json");

			body = RequestBody.create(mediaType, "");
			
			request = new Request.Builder()
					.url(url+"/services/data/v50.0/sobjects/Opportunity/" + opportunityId)
					.method("DELETE", body).addHeader("Authorization", "Bearer " + bearerToken)
					.addHeader("Content-Type", "application/json").build();

			   // Read from request
			response = client.newCall(request).execute();
			
			assertEquals(response.message(), "No Content");
			assertEquals(response.code(), 204);
			
		} catch (Exception e) {
			System.out.println("API error" + e.getMessage());
			Assert.fail();
		}
	}
	
	/**
	 * @param token
	 * @param url
	 * @param campaignName
	 * @param contactId
	 * @return
	 */
	public static String createCampaign(String token, String url, String campaignName) {
	
		String campaignId = null;
		try {
			client = new OkHttpClient().newBuilder().connectTimeout(10, TimeUnit.SECONDS)
				    .writeTimeout(10, TimeUnit.SECONDS)
				    .readTimeout(30, TimeUnit.SECONDS)
				    .build();
			mediaType = MediaType.parse("application/json");

			body = RequestBody.create(mediaType,
					"{\n\"Name\":\""+campaignName+"\",\n"
					+ "\"IsActive\": \"true\"}");
			request = new Request.Builder()
					.url(url+"/services/data/v50.0/sobjects/Campaign")
					.method("POST", body).addHeader("Authorization", "Bearer " + token)
					.addHeader("Content-Type", "application/json").build();

			// Read from request
			response = client.newCall(request).execute();
			String jsonData = response.body().string();
		    JSONObject Jobject = new JSONObject(jsonData);
		    campaignId  = Jobject.get("id").toString();

		    assertTrue(Strings.isNotNullAndNotEmpty(campaignId));
			assertEquals(response.message(), "Created");
			assertEquals(response.code(), 201);
			
		} catch (Exception e) {
			System.out.println("API error" + e.getMessage());
			Assert.fail();
		}
		return campaignId;
	}
	
	/**
	 * @param token
	 * @param url
	 * @param campaignId
	 * @param contactId
	 * @return
	 */
	public static String addContactInCampaign(String token, String url, String campaignId, String contactId ) {
		
		String participantCampaignId = null;
		try {
			client = new OkHttpClient().newBuilder().connectTimeout(10, TimeUnit.SECONDS)
				    .writeTimeout(10, TimeUnit.SECONDS)
				    .readTimeout(30, TimeUnit.SECONDS)
				    .build();
			mediaType = MediaType.parse("application/json");

			body = RequestBody.create(mediaType,
					"{\n\"CampaignId\":\""+campaignId+"\",\n"
					 + "\"ContactId\" : \""+contactId+"\"}");
			request = new Request.Builder()
					.url(url+"/services/data/v50.0/sobjects/CampaignMember")
					.method("POST", body).addHeader("Authorization", "Bearer " + token)
					.addHeader("Content-Type", "application/json").build();

			// Read from request
			response = client.newCall(request).execute();
			String jsonData = response.body().string();
		    JSONObject Jobject = new JSONObject(jsonData);
		    participantCampaignId  = Jobject.get("id").toString();

		    assertTrue(Strings.isNotNullAndNotEmpty(participantCampaignId));
			assertEquals(response.message(), "Created");
			assertEquals(response.code(), 201);
			
		} catch (Exception e) {
			System.out.println("API error" + e.getMessage());
			Assert.fail();
		}
		return participantCampaignId;
	}
	
	/**
	 * @param token
	 * @param url
	 * @param campaignId
	 * @param leadId
	 * @return
	 */
	public static String addLeadInCampaign(String token, String url, String campaignId, String leadId ) {
		
		String participantCampaignId = null;
		try {
			client = new OkHttpClient().newBuilder().connectTimeout(10, TimeUnit.SECONDS)
				    .writeTimeout(10, TimeUnit.SECONDS)
				    .readTimeout(30, TimeUnit.SECONDS)
				    .build();
			mediaType = MediaType.parse("application/json");

			body = RequestBody.create(mediaType,
					"{\n\"CampaignId\":\""+campaignId+"\",\n"
					 + "\"LeadId\" : \""+leadId+"\"}");
			request = new Request.Builder()
					.url(url+"/services/data/v50.0/sobjects/CampaignMember")
					.method("POST", body).addHeader("Authorization", "Bearer " + token)
					.addHeader("Content-Type", "application/json").build();

			// Read from request
			response = client.newCall(request).execute();
			String jsonData = response.body().string();
		    JSONObject Jobject = new JSONObject(jsonData);
		    participantCampaignId  = Jobject.get("id").toString();

		    assertTrue(Strings.isNotNullAndNotEmpty(participantCampaignId));
			assertEquals(response.message(), "Created");
			assertEquals(response.code(), 201);
			
		} catch (Exception e) {
			System.out.println("API error" + e.getMessage());
			Assert.fail();
		}
		return participantCampaignId;
	}
	
	/**
	 * @param url
	 * @param bearerToken
	 * @param campaignId
	 * @param body
	 */
	public static void updateCampaign(String url, String bearerToken, String campaignId, RequestBody body) {
		try {
			client = new OkHttpClient().newBuilder().connectTimeout(10, TimeUnit.SECONDS)
				    .writeTimeout(10, TimeUnit.SECONDS)
				    .readTimeout(30, TimeUnit.SECONDS)
				    .build();
			mediaType = MediaType.parse("application/json");

			request = new Request.Builder()
					.url(url+"/services/data/v50.0/sobjects/Campaign/" + campaignId)
					.method("PATCH", body).addHeader("Authorization", "Bearer " + bearerToken)
					.addHeader("Content-Type", "application/json").build();

			   // Read from request
			response = client.newCall(request).execute();
		    
			assertEquals(response.message(), "No Content");
			assertEquals(response.code(), 204);
			
			Thread.sleep(5*1000);
			
		} catch (Exception e) {
			System.out.println("API error" + e.getMessage());
		}
	}


	/**
	 * @param url
	 * @param bearerToken
	 * @param campaignId
	 */
	public static void deleteCampaign(String url, String bearerToken, String campaignId) {
		try {
			client = new OkHttpClient().newBuilder().connectTimeout(10, TimeUnit.SECONDS)
				    .writeTimeout(10, TimeUnit.SECONDS)
				    .readTimeout(30, TimeUnit.SECONDS)
				    .build();
			mediaType = MediaType.parse("application/json");

			body = RequestBody.create(mediaType, "");
			
			request = new Request.Builder()
					.url(url+"/services/data/v50.0/sobjects/Campaign/" + campaignId)
					.method("DELETE", body).addHeader("Authorization", "Bearer " + bearerToken)
					.addHeader("Content-Type", "application/json").build();

			   // Read from request
			response = client.newCall(request).execute();
			
			assertEquals(response.message(), "No Content");
			assertEquals(response.code(), 204);
			
		} catch (Exception e) {
			System.out.println("API error" + e.getMessage());
			Assert.fail();
		}
	}
	
	/**
	 * @param url
	 * @param userName
	 * @param password
	 * @return
	 */
	public static String getBearerToken(String url, String userName, String password) {
		String bearerToken = null;
		try {
			client = new OkHttpClient().newBuilder().connectTimeout(10, TimeUnit.SECONDS)
				    .writeTimeout(20, TimeUnit.SECONDS)
				    .readTimeout(40, TimeUnit.SECONDS)
				    .build();

			mediaType = MediaType.parse("text/xml");
			body = RequestBody.create(mediaType,
					"<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n<env:Envelope xmlns:xsd=\"https://www.w3.org/2001/XMLSchema\"\n    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n    xmlns:env=\"http://schemas.xmlsoap.org/soap/envelope/\">\n  <env:Body>\n    <n1:login xmlns:n1=\"urn:partner.soap.sforce.com\">\n      "
					+ "<n1:username>"+userName+"</n1:username>\n      "
					+ "<n1:password>"+password+"</n1:password>\n    </n1:login>\n  </env:Body>\n</env:Envelope>");
			request = new Request.Builder().url(url+"/services/Soap/u/50.0")
					.method("POST", body).addHeader("Content-Type", "text/xml").addHeader("SOAPAction", "login")
					.addHeader("charset", "UTF-8").build();

			response = client.newCall(request).execute();
			jsonData = response.body().string();
			
			doc = Jsoup.parse(jsonData, "", Parser.xmlParser());
			bearerToken = doc.select("sessionId").text();
			
			assertTrue(Strings.isNotNullAndNotEmpty(bearerToken), "bearerToken is null");
		} catch (Exception e) {
			System.out.println("API error" + e.getMessage());
			Assert.fail();
		}
		return bearerToken;
	}
	
	public static String getSesstionID (String app_url, String userName, String password) {
		String sessionId = null;
		try {
			client = new OkHttpClient().newBuilder().connectTimeout(10, TimeUnit.SECONDS)
				    .writeTimeout(20, TimeUnit.SECONDS)
				    .readTimeout(40, TimeUnit.SECONDS)
				    .build();

			mediaType = MediaType.parse("text/plain");
			body = RequestBody.create(mediaType,"");
			request = new Request.Builder().url(app_url+"/api/v2/session/authenticateUsernamePassword?username="+userName+"&password="+password+"")
					.method("POST", body).build();

			// Read from request
			response = client.newCall(request).execute();
			String jsonData = response.body().string();
			JSONObject Jobject = new JSONObject(jsonData);
			sessionId = Jobject.get("sessionId").toString();
			
			assertTrue(Strings.isNotNullAndNotEmpty(sessionId));
			assertEquals(response.code(), 200);
		} catch (Exception e) {
			System.out.println("API error" + e.getMessage());
			Assert.fail();
		}
		
		return sessionId;
		
	}
	
	public static String getNoteTemplates (String app_url, String accountId, String sessionId, String templateName) {
		String jsonData = null;
		String Idval = null ;
	    String nameval = null;

		try {
			client = new OkHttpClient().newBuilder().connectTimeout(10, TimeUnit.SECONDS)
				    .writeTimeout(20, TimeUnit.SECONDS)
				    .readTimeout(40, TimeUnit.SECONDS)
				    .build();

			mediaType = MediaType.parse("text/plain");
			body = RequestBody.create(mediaType,"");
			request = new Request.Builder().url(app_url+"/api/v2/app/"+templateName+"?accountId="+accountId+"")
					.method("GET", null).addHeader("sessionId", ""+sessionId+"").build();

			// Read from request
			response = client.newCall(request).execute();
			jsonData = response.body().string();
			String jsonDataVal = jsonData.substring(1, jsonData.length()-1);
		    JSONObject Jobject = new JSONObject(jsonDataVal);
		    Idval  = Jobject.get("id").toString();
		    nameval  = Jobject.get("name").toString();

			
			assertTrue(Strings.isNotNullAndNotEmpty(sessionId));
			assertEquals(response.code(), 200);
		} catch (Exception e) {
			System.out.println("API error" + e.getMessage());
			Assert.fail();
		}
		
		return Idval+","+ nameval;
	}
	
	public static String getresponcevalue (String key, String json) throws JSONException {
		String jsonDataVal = json.substring(1, json.length()-1);
			JSONObject json1 = new JSONObject(jsonDataVal);
			boolean exists = json1.has(key);
		Iterator<?> keys;
		String nextkeys = null;
		
		if(!exists) {
			keys = json1.keys();
			while (keys.hasNext()) {
				nextkeys = (String)keys.next();
				try {
					if (json1.get(nextkeys)instanceof JSONObject) {
						if (exists == false) {
							getKey(json1.getJSONObject(nextkeys), key);
						}
					}else if (json1.get(nextkeys)instanceof JSONArray) {
						JSONArray jsonarray = json1.getJSONArray(nextkeys);
						for(int i=0; i<jsonarray.length(); i++) {
							String jsonarrayString = jsonarray.get(i).toString();
							JSONObject innerjson = new JSONObject(jsonarrayString);
							if (exists == false) {
								getKey(innerjson, key);
							}
						}
					}
				}catch (Exception e) {
					
				//TODO
			}
		}
		}
		return nextkeys;
		
	}
	
	
	
	
	

	private static void getKey(JSONObject jsonObject, String key) {
		// TODO Auto-generated method stub
		
	}

	public static String createCallAction(String token, String url, String actionName,String idNameValue ) {
		String callActionId = null;
		try {
			String[] arrIdName = {} ;
			arrIdName = idNameValue.split(",");
			String Template_Id = arrIdName[0];
			String Template_Name = arrIdName[1];
			client = new OkHttpClient().newBuilder().connectTimeout(10, TimeUnit.SECONDS)
				    .writeTimeout(10, TimeUnit.SECONDS)
				    .readTimeout(30, TimeUnit.SECONDS)
				    .build();
			mediaType = MediaType.parse("application/json");
			
			 body = RequestBody.create(mediaType, "{\n\"Name\":\""+actionName+"\",\n\"RDNACadence__Type__c\": \"Call\","
					+ "\n\"RDNACadence__Template_Name__c\": \""+Template_Name+"\","
					+ "\n\"RDNACadence__Template_Id__c\": \""+Template_Id+"\","
					+ "\n\"RDNACadence__Activation_Type__c\": \"Manual\"\n}\n");
			request = new Request.Builder()
					.url(url+"/services/data/v50.0/sobjects/RDNACadence__Action__c")
					.method("POST", body).addHeader("Authorization", "Bearer " + token)
					.addHeader("Content-Type", "application/json").build();

			   // Read from request
			response = client.newCall(request).execute();
			String jsonData = response.body().string();
		    JSONObject Jobject = new JSONObject(jsonData);
		   callActionId  = Jobject.get("id").toString();

		    assertTrue(Strings.isNotNullAndNotEmpty(callActionId));
			assertEquals(response.message(), "Created");
			assertEquals(response.code(), 201);
			
		} catch (Exception e) {
			System.out.println("API error" + e.getMessage());
			Assert.fail();
		}
		return callActionId;
	}
	
	public static void updateAction(String token, String url,String ActionId) {
		try {
			client = new OkHttpClient().newBuilder().connectTimeout(10, TimeUnit.SECONDS)
				    .writeTimeout(10, TimeUnit.SECONDS)
				    .readTimeout(30, TimeUnit.SECONDS)
				    .build();
			mediaType = MediaType.parse("application/json");
			
			 body = RequestBody.create(mediaType, "{\n\t\"Name\":\"Call_Action_Postman_Edit1\"\n    \n}");
			 request = new Request.Builder()
			  .url(url+"/services/data/v48.0/sobjects/RDNACadence__Action__c/"+ActionId+"")
			  .method("PATCH", body)
			  .addHeader("Authorization", "Bearer " + token)
			  .addHeader("Content-Type", "application/json").build();

		   // Read from requestd
			response = client.newCall(request).execute();

			assertEquals(response.code(), 204);
			
		} catch (Exception e) {
			System.out.println("API error" + e.getMessage());
			Assert.fail();
		}
	
	}
	
	public static void deleteAction(String token, String url,String ActionId) {
		try {
			client = new OkHttpClient().newBuilder().connectTimeout(10, TimeUnit.SECONDS)
				    .writeTimeout(10, TimeUnit.SECONDS)
				    .readTimeout(30, TimeUnit.SECONDS)
				    .build();
			mediaType = MediaType.parse("application/json");
			
			 body = RequestBody.create(mediaType, "");
			 request = new Request.Builder()
			  .url(url+"/services/data/v48.0/sobjects/RDNACadence__Action__c/"+ActionId+"")
			  .method("DELETE", body)
			  .addHeader("Authorization", "Bearer " + token)
			  .addHeader("Content-Type", "application/json").build();

		   // Read from requestd
			response = client.newCall(request).execute();

			assertEquals(response.code(), 204);
			
		} catch (Exception e) {
			System.out.println("API error" + e.getMessage());
			Assert.fail();
		}
	
	}
	
	
		public static String createSmsAction(String token, String url, String actionName,String idNameValue) {
			String smsActionId = null;
			try {
				String[] arrIdName = {} ;
				arrIdName = idNameValue.split(",");
				String Template_Id = arrIdName[0];
				String Template_Name = arrIdName[1];
				client = new OkHttpClient().newBuilder().connectTimeout(10, TimeUnit.SECONDS)
					    .writeTimeout(10, TimeUnit.SECONDS)
					    .readTimeout(30, TimeUnit.SECONDS)
					    .build();
				mediaType = MediaType.parse("application/json");
				
				 body = RequestBody.create(mediaType, "{\n\"Name\":\""+actionName+"\",\n\"RDNACadence__Type__c\": \"SMS\","
						+ "\n\"RDNACadence__Template_Name__c\": \""+Template_Name+"\","
						+ "\n\"RDNACadence__Template_Id__c\": \""+Template_Id+"\","
						+ "\n\"RDNACadence__Activation_Type__c\": \"Manual\"\n}\n");
				request = new Request.Builder()
						.url(url+"/services/data/v50.0/sobjects/RDNACadence__Action__c")
						.method("POST", body).addHeader("Authorization", "Bearer " + token)
						.addHeader("Content-Type", "application/json").build();

				   // Read from request
				response = client.newCall(request).execute();
				String jsonData = response.body().string();
			    JSONObject Jobject = new JSONObject(jsonData);
			    smsActionId  = Jobject.get("id").toString();

			    assertTrue(Strings.isNotNullAndNotEmpty(smsActionId));
				assertEquals(response.message(), "Created");
				assertEquals(response.code(), 201);
				
			} catch (Exception e) {
				System.out.println("API error" + e.getMessage());
				Assert.fail();
			}
			return smsActionId;
			
		
	}
		
		public static String createTaskAction(String token, String url, String actionName) {
			String taskActionId = null;
			try {
				client = new OkHttpClient().newBuilder().connectTimeout(10, TimeUnit.SECONDS)
					    .writeTimeout(10, TimeUnit.SECONDS)
					    .readTimeout(30, TimeUnit.SECONDS)
					    .build();
				mediaType = MediaType.parse("application/json");
				
				 body = RequestBody.create(mediaType, "{\n\"Name\":\""+actionName+"\","
				 		+ "\n\"RDNACadence__Type__c\": \"Task\","
				 		+ "\n\"RDNACadence__Task_Description__c\":\"{\\\"attributes\\\":{\\\"type\\\":\\\"Task\\\"},"
				 		+ "\\\"Status\\\":\\\"In Progress\\\",\\\"Subject\\\":\\\"Test Sub 01\\\"}\","
				 		+ "\n\"RDNACadence__Activation_Type__c\": \"Manual\"\n}\n");
				request = new Request.Builder()
						.url(url+"/services/data/v50.0/sobjects/RDNACadence__Action__c")
						.method("POST", body).addHeader("Authorization", "Bearer " + token)
						.addHeader("Content-Type", "application/json").build();

				   // Read from request
				response = client.newCall(request).execute();
				String jsonData = response.body().string();
			    JSONObject Jobject = new JSONObject(jsonData);
			    taskActionId  = Jobject.get("id").toString();

			    assertTrue(Strings.isNotNullAndNotEmpty(taskActionId));
				assertEquals(response.message(), "Created");
				assertEquals(response.code(), 201);
				
			} catch (Exception e) {
				System.out.println("API error" + e.getMessage());
				Assert.fail();
			}
			return taskActionId;
		
	}
		//Activation_Type  Automatic and Manual
		
		public static String createAutoEmailAction(String token, String url, String actionName,String Activation_Type) {
			String Template_Id = TestBase.CONFIG.getProperty("Template_Id");
		
			String autoEmailActionId = null;
			try {
				client = new OkHttpClient().newBuilder().connectTimeout(10, TimeUnit.SECONDS)
					    .writeTimeout(10, TimeUnit.SECONDS)
					    .readTimeout(30, TimeUnit.SECONDS)
					    .build();
				mediaType = MediaType.parse("application/json");
				
				RequestBody body = RequestBody.create(mediaType, "{\n\"Name\":\""+actionName+"\","
						+ "\n\"RDNACadence__Type__c\": \"Email\","
						+ "\n\"RDNACadence__Email_Type__c\": \"NATIVE\","
						+ "\n\"RDNACadence__Activation_Type__c\": \""+Activation_Type+"\","
						+ "\n\"RDNACadence__Delivery_Preference__c\": \"Immediate\","
						+ "\n\"RDNACadence__Template_Id__c\": \""+Template_Id+"\"\n}\n");
						
				request = new Request.Builder()
						.url(url+"/services/data/v50.0/sobjects/RDNACadence__Action__c")
						.method("POST", body).addHeader("Authorization", "Bearer " + token)
						.addHeader("Content-Type", "application/json").build();

				   // Read from request
				response = client.newCall(request).execute();
				String jsonData = response.body().string();
			    JSONObject Jobject = new JSONObject(jsonData);
			    autoEmailActionId  = Jobject.get("id").toString();

			    assertTrue(Strings.isNotNullAndNotEmpty(autoEmailActionId));
				assertEquals(response.message(), "Created");
				assertEquals(response.code(), 201);
				
			} catch (Exception e) {
				System.out.println("API error" + e.getMessage());
				Assert.fail();
			}
			return autoEmailActionId;
		
	}
		public static String createManualEmailAction(String token, String url, String actionName) {
			String munalEmailActionId = null;
			try {
				client = new OkHttpClient().newBuilder().connectTimeout(10, TimeUnit.SECONDS)
					    .writeTimeout(10, TimeUnit.SECONDS)
					    .readTimeout(30, TimeUnit.SECONDS)
					    .build();
				mediaType = MediaType.parse("application/json");
				
				 body = RequestBody.create(mediaType, "{\n\"Name\":\""+actionName+"\","
						+ "\n\"RDNACadence__Type__c\": \"Email\","
						+ "\n\"RDNACadence__Email_Type__c\": \"NATIVE\","
						+ "\n\"RDNACadence__Activation_Type__c\": \"Manual\","
						+ "\n\"RDNACadence__Template_Id__c\": \"a7u010000004LHhAAM\","
						+ "\n\"RDNACadence__Template_Name__c\": \"QA-HTMLTemplatePublicDeep\"\n\n}\n");
				request = new Request.Builder()
						.url(url+"/services/data/v50.0/sobjects/RDNACadence__Action__c")
						.method("POST", body).addHeader("Authorization", "Bearer " + token)
						.addHeader("Content-Type", "application/json").build();

				   // Read from request
				response = client.newCall(request).execute();
				String jsonData = response.body().string();
			    JSONObject Jobject = new JSONObject(jsonData);
			    munalEmailActionId  = Jobject.get("id").toString();

			    assertTrue(Strings.isNotNullAndNotEmpty(munalEmailActionId));
				assertEquals(response.message(), "Created");
				assertEquals(response.code(), 201);
				
			} catch (Exception e) {
				System.out.println("API error" + e.getMessage());
				Assert.fail();
			}
			return munalEmailActionId;
		
	}
		//Create Lead Sequence
		public static String createSequence(String token, String url, String sequenceName,String entranceCriteria, String exitCriteria, String SequenceType ) {
			String SequenceId = null;
			try {
				client = new OkHttpClient().newBuilder().connectTimeout(10, TimeUnit.SECONDS)
					    .writeTimeout(10, TimeUnit.SECONDS)
					    .readTimeout(30, TimeUnit.SECONDS)
					    .build();
				mediaType = MediaType.parse("application/json");
				
				body = RequestBody.create(mediaType, "{\n\"Name\":\""+sequenceName+"\",\n\"RDNACadence__Record_Type__c\":\"Lead\","
						+ "\n\"RDNACadence__matching_priority__c\": \"P1\","
						+ "\n\"RDNACadence__Participent_Activation__c\": \""+SequenceType+"\","
						+ "\n\"RDNACadence__Entrance_Criteria__c\": \"{\\\"criterionPriorityFormula\\\":\\\"\\\","
						+ "\\\"condition\\\":\\\"All of the conditions are met (AND)\\\",\\\"executionCriterion\\\":\\\"Conditions are met\\\","
						+ "\\\"criterions\\\":{\\\"1\\\":{\\\"fieldDataType\\\":\\\"String\\\","
						+ "\\\"fieldName\\\":\\\"FirstName\\\",\\\"fieldLabel\\\":\\\"First Name\\\",\\\"operation\\\":\\\"equals\\\","
						+ "\\\"value\\\":\\\""+entranceCriteria+"\\\",\\\"id\\\":1}}}\","
						+ "\n\"RDNACadence__Exit_Criteria__c\": \"{\\\"criterionPriorityFormula\\\":\\\"\\\","
						+ "\\\"condition\\\":\\\"All of the conditions are met (AND)\\\","
						+ "\\\"executionCriterion\\\":\\\"Conditions are met\\\","
						+ "\\\"criterions\\\":{\\\"1\\\":{\\\"fieldDataType\\\":\\\"String\\\","
						+ "\\\"fieldName\\\":\\\"LastName\\\",\\\"fieldLabel\\\":\\\"Last Name\\\","
						+ "\\\"operation\\\":\\\"equals\\\",\\\"value\\\":\\\"Enter SFDC-378433\\\",\\\"id\\\":1}}}\","
						+ "\n\"RDNACadence__Status__c\": true,"
						+ "\n\"RDNACadence__Delegated_Owner_Lookup__c\": \"createdbyid\"\n}\n");
						
				request = new Request.Builder()
						.url(url+"/services/data/v50.0/sobjects/RDNACadence__Cadence__c")
						.method("POST", body).addHeader("Authorization", "Bearer " + token)
						.addHeader("Content-Type", "application/json").build();

				   // Read from request
				response = client.newCall(request).execute();
				String jsonData = response.body().string();
			    JSONObject Jobject = new JSONObject(jsonData);
			    SequenceId  = Jobject.get("id").toString();

			    assertTrue(Strings.isNotNullAndNotEmpty(SequenceId));
				assertEquals(response.message(), "Created");
				assertEquals(response.code(), 201);
				
			} catch (Exception e) {
				System.out.println("API error" + e.getMessage());
				Assert.fail();
			}
			return SequenceId;
		
	}
		
		//Create Contact Sequence
				public static String createcontactSequence(String token, String url, String sequenceName,String entranceCriteria, String exitCriteria, String SequenceType ) {
					String SequenceId = null;
					try {
						client = new OkHttpClient().newBuilder().connectTimeout(10, TimeUnit.SECONDS)
							    .writeTimeout(10, TimeUnit.SECONDS)
							    .readTimeout(30, TimeUnit.SECONDS)
							    .build();
						mediaType = MediaType.parse("application/json");
						
						body = RequestBody.create(mediaType, "{\n\"Name\":\""+sequenceName+"\",\n\"RDNACadence__Record_Type__c\":\"Contact\","
								+ "\n\"RDNACadence__matching_priority__c\": \"P1\","
								+ "\n\"RDNACadence__Participent_Activation__c\": \""+SequenceType+"\","
								+ "\n\"RDNACadence__Entrance_Criteria__c\": \"{\\\"criterionPriorityFormula\\\":\\\"\\\","
								+ "\\\"condition\\\":\\\"All of the conditions are met (AND)\\\",\\\"executionCriterion\\\":\\\"Conditions are met\\\","
								+ "\\\"criterions\\\":{\\\"1\\\":{\\\"fieldDataType\\\":\\\"String\\\","
								+ "\\\"fieldName\\\":\\\"FirstName\\\",\\\"fieldLabel\\\":\\\"First Name\\\",\\\"operation\\\":\\\"equals\\\","
								+ "\\\"value\\\":\\\""+entranceCriteria+"\\\",\\\"id\\\":1}}}\","
								+ "\n\"RDNACadence__Exit_Criteria__c\": \"{\\\"criterionPriorityFormula\\\":\\\"\\\","
								+ "\\\"condition\\\":\\\"All of the conditions are met (AND)\\\","
								+ "\\\"executionCriterion\\\":\\\"Conditions are met\\\","
								+ "\\\"criterions\\\":{\\\"1\\\":{\\\"fieldDataType\\\":\\\"String\\\","
								+ "\\\"fieldName\\\":\\\"LastName\\\",\\\"fieldLabel\\\":\\\"Last Name\\\","
								+ "\\\"operation\\\":\\\"equals\\\",\\\"value\\\":\\\"Enter SFDC-378433\\\",\\\"id\\\":1}}}\","
								+ "\n\"RDNACadence__Status__c\": true,"
								+ "\n\"RDNACadence__Delegated_Owner_Lookup__c\": \"createdbyid\"\n}\n");
								
						request = new Request.Builder()
								.url(url+"/services/data/v50.0/sobjects/RDNACadence__Cadence__c")
								.method("POST", body).addHeader("Authorization", "Bearer " + token)
								.addHeader("Content-Type", "application/json").build();

						   // Read from request
						response = client.newCall(request).execute();
						String jsonData = response.body().string();
					    JSONObject Jobject = new JSONObject(jsonData);
					    SequenceId  = Jobject.get("id").toString();

					    assertTrue(Strings.isNotNullAndNotEmpty(SequenceId));
						assertEquals(response.message(), "Created");
						assertEquals(response.code(), 201);
						
					} catch (Exception e) {
						System.out.println("API error" + e.getMessage());
						Assert.fail();
					}
					return SequenceId;
				
			}
				
				//Create Opp Sequence Automatic
				public static String createOppSequence(String token, String url, String sequenceName,String entranceCriteria, String exitCriteria, String SequenceType ) {
					String SequenceId = null;
					try {
						client = new OkHttpClient().newBuilder().connectTimeout(10, TimeUnit.SECONDS)
							    .writeTimeout(10, TimeUnit.SECONDS)
							    .readTimeout(30, TimeUnit.SECONDS)
							    .build();
						mediaType = MediaType.parse("application/json");
						
						 body = RequestBody.create(mediaType, "{\n\"Name\":\""+sequenceName+"\",\n\"RDNACadence__Record_Type__c\":\"Opportunity\","
								+ "\n\"RDNACadence__matching_priority__c\": \"P1\",\n\"RDNACadence__Participent_Activation__c\": \""+SequenceType+"\","
								+ "\n \"RDNACadence__Entrance_Criteria__c\": \"{\\\"condition\\\":\\\"All of the conditions are met (AND)\\\","
								+ "\\\"criterionPriorityFormula\\\":\\\"\\\",\\\"executionCriterion\\\":\\\"Conditions are met\\\","
								+ "\\\"criterions\\\":{\\\"1\\\":{\\\"fieldDataType\\\":\\\"String\\\",\\\"fieldName\\\":\\\"Opportunity.Name\\\","
								+ "\\\"fieldLabel\\\":\\\"Name\\\",\\\"operation\\\":\\\"equals\\\",\\\"value\\\":\\\""+entranceCriteria+"\\\",\\\"id\\\":1}}}\","
								+ "\n \"RDNACadence__Status__c\": true,\n \"RDNACadence__Exit_Criteria__c\": \"{\\\"condition\\\":\\\"All of the conditions are met (AND)\\\","
								+ "\\\"criterionPriorityFormula\\\":\\\"\\\",\\\"executionCriterion\\\":\\\"Conditions are met\\\","
								+ "\\\"criterions\\\":{\\\"1\\\":{\\\"fieldDataType\\\":\\\"Picklist\\\",\\\"fieldName\\\":\\\"Opportunity.StageName\\\","
								+ "\\\"fieldLabel\\\":\\\"Stage\\\",\\\"operation\\\":\\\"equals\\\","
								+ "\\\"value\\\":\\\"3 - Business Case\\\",\\\"id\\\":1}}}\"\n \n}\n");
								
						request = new Request.Builder()
								.url(url+"/services/data/v50.0/sobjects/RDNACadence__Cadence__c")
								.method("POST", body).addHeader("Authorization", "Bearer " + token)
								.addHeader("Content-Type", "application/json").build();

						   // Read from request
						response = client.newCall(request).execute();
						String jsonData = response.body().string();
					    JSONObject Jobject = new JSONObject(jsonData);
					    SequenceId  = Jobject.get("id").toString();

					    assertTrue(Strings.isNotNullAndNotEmpty(SequenceId));
						assertEquals(response.message(), "Created");
						assertEquals(response.code(), 201);
						
					} catch (Exception e) {
						System.out.println("API error" + e.getMessage());
						Assert.fail();
					}
					return SequenceId;
				
			}
				
				//Create Opp Sequence
				// SequenceType Automatic and Manual
				public static String createCampSequence(String token, String url, String sequenceName,String entranceCriteria, String exitCriteria, String SequenceType ) {
					String SequenceId = null;
					try {
						client = new OkHttpClient().newBuilder().connectTimeout(10, TimeUnit.SECONDS)
							    .writeTimeout(10, TimeUnit.SECONDS)
							    .readTimeout(30, TimeUnit.SECONDS)
							    .build();
						mediaType = MediaType.parse("application/json");
						
						RequestBody body = RequestBody.create(mediaType, "{\n\"Name\":\""+sequenceName+"\",\n\"RDNACadence__Record_Type__c\":\"Campaign\","
								+ "\n\"RDNACadence__matching_priority__c\": \"P1\",\n\"RDNACadence__Participent_Activation__c\": \""+SequenceType+"\","
								+ "\n \"RDNACadence__Entrance_Criteria__c\": \"{\\\"criterionPriorityFormula\\\":\\\"\\\",\\\"condition\\\":\\\"All of the conditions are met (AND)\\\","
								+ "\\\"executionCriterion\\\":\\\"Conditions are met\\\",\\\"criterions\\\":{\\\"1\\\":{\\\"fieldDataType\\\":\\\"String\\\","
								+ "\\\"fieldName\\\":\\\"Campaign.Name\\\",\\\"fieldLabel\\\":\\\"\\\",\\\"operation\\\":\\\"equals\\\",\\\"value\\\":\\\""+entranceCriteria+"\\\","
								+ "\\\"secondValue\\\":\\\"\\\",\\\"id\\\":1}}}\",\n \"RDNACadence__Exit_Criteria__c\": \"{\\\"criterionPriorityFormula\\\":\\\"\\\","
								+ "\\\"condition\\\":\\\"All of the conditions are met (AND)\\\",\\\"executionCriterion\\\":\\\"Conditions are met\\\","
								+ "\\\"criterions\\\":{\\\"1\\\":{\\\"fieldDataType\\\":\\\"String\\\",\\\"fieldName\\\":\\\"Campaign.Name\\\",\\\"fieldLabel\\\":\\\"\\\","
								+ "\\\"operation\\\":\\\"equals\\\",\\\"value\\\":\\\"Campaign_Name_Postman\\\","
								+ "\\\"secondValue\\\":\\\"\\\",\\\"id\\\":1}}}\",\n  \"RDNACadence__Status__c\": true\n}\n");
								
						request = new Request.Builder()
								.url(url+"/services/data/v50.0/sobjects/RDNACadence__Cadence__c")
								.method("POST", body).addHeader("Authorization", "Bearer " + token)
								.addHeader("Content-Type", "application/json").build();

						   // Read from request
						response = client.newCall(request).execute();
						String jsonData = response.body().string();
					    JSONObject Jobject = new JSONObject(jsonData);
					    SequenceId  = Jobject.get("id").toString();

					    assertTrue(Strings.isNotNullAndNotEmpty(SequenceId));
						assertEquals(response.message(), "Created");
						assertEquals(response.code(), 201);
						
					} catch (Exception e) {
						System.out.println("API error" + e.getMessage());
						Assert.fail();
					}
					return SequenceId;
				
			}
		
		// @Delegated_Owner 
			//createdbyid
			//lastmodifiedbyid
			//sdr_owner__c
			//custom_lookup1__c
			//custom_lookup2__c
			//ownerid
		public static String addActionsequence(String token, String url, String sequence_Id, String actionID, String Delegated_Owner, HashMap<String, String> UpdateMapData ) {
			String actinSequenceId = null;
			
			try {
				client = new OkHttpClient().newBuilder().connectTimeout(10, TimeUnit.SECONDS)
					    .writeTimeout(10, TimeUnit.SECONDS)
					    .readTimeout(30, TimeUnit.SECONDS)
					    .build();
				mediaType = MediaType.parse("application/json");
				
				
				RequestBody body = RequestBody.create(mediaType, "{\n\"RDNACadence__Cadence_Id__c\": \""+sequence_Id+"\","
						+ "\n\"RDNACadence__Action_Id__c\": \""+actionID+"\",\n\"RDNACadence__Priority__c\": \"Medium\","
						+ "\n\"RDNACadence__Trigger_Type__c\": \"Immediate\",\n\"RDNACadence__Fields_To_Update_Action__c\":\"{\\\"sobjectType\\\":\\\""+UpdateMapData.get("sequence_type")+"\\\","
						+ "\\\""+UpdateMapData.get("field_Name")+"\\\":\\\""+UpdateMapData.get("field_Value")+"\\\",\\\""+UpdateMapData.get("secondField_Name")+"\\\":\\\""+UpdateMapData.get("secondField_Value")+"\\\"}\","
						+ "\n\"RDNACADENCE__DELEGATED_OWNER_LOOKUP__C\" : \""+Delegated_Owner+"\"\n}\n\n\n\n\n\n\n");
				
				request = new Request.Builder()
						.url(url+"/services/data/v50.0/sobjects/RDNACadence__CadenceAction__c")
						.method("POST", body).addHeader("Authorization", "Bearer " + token)
						.addHeader("Content-Type", "application/json").build();

				   // Read from request
				response = client.newCall(request).execute();
				String jsonData = response.body().string();
			    JSONObject Jobject = new JSONObject(jsonData);
			    actinSequenceId  = Jobject.get("id").toString();

			    assertTrue(Strings.isNotNullAndNotEmpty(actinSequenceId));
				assertEquals(response.message(), "Created");
				assertEquals(response.code(), 201);
				
			} catch (Exception e) {
				System.out.println("API error" + e.getMessage());
				Assert.fail();
			}
			return actinSequenceId;
	}
		
		

		public static  String verifyLeadObjfield(String token, String url, String leadId, String queryField, String queryObjName  ) {
			
			String value = null;
			try {
				client = new OkHttpClient().newBuilder().connectTimeout(10, TimeUnit.SECONDS)
					    .writeTimeout(10, TimeUnit.SECONDS)
					    .readTimeout(30, TimeUnit.SECONDS)
					    .build();
				
				request = new Request.Builder().url(
						url+"services/data/v50.0/query/?q=select+"+queryField+"+from+"+queryObjName+"+where+Id+=+'"+leadId+"'")
						.method("GET", null)
						.addHeader("Authorization","Bearer " + token)
						.addHeader("Content-Type", "application/json")
						.build();

				   // Read from request
				response = client.newCall(request).execute();
				String jsonData = response.body().string();
			    JSONObject Jobject = new JSONObject(jsonData);
			    
			     JSONArray result = Jobject.getJSONArray("records");
			     JSONObject fielsValue = result.getJSONObject(0);
			     
			    value  = fielsValue.get(queryField).toString();

			    assertTrue(Strings.isNotNullAndNotEmpty(value));
				assertEquals(response.code(), 200);
				
			} catch (Exception e) {
				System.out.println("API error" + e.getMessage());
				Assert.fail();
			}
			return value;
			
			
		}
		
		
		public static  HashMap<String, String> GetParticipantActionsID(String token, String url, String leadId, String queryField, String queryObjName, String Objectfield  ) {
			
			HashMap <String, String> ParticipantActionsID = new HashMap <String, String>();
			try {
				client = new OkHttpClient().newBuilder().connectTimeout(10, TimeUnit.SECONDS)
					    .writeTimeout(10, TimeUnit.SECONDS)
					    .readTimeout(30, TimeUnit.SECONDS)
					    .build();
				
				request = new Request.Builder().url(
						url+"services/data/v50.0/query/?q=select+"+queryField+"+from+"+queryObjName+"+where+"+Objectfield+"+=+'"+leadId+"'")
						.method("GET", null)
						.addHeader("Authorization","Bearer " + token)
						.addHeader("Content-Type", "application/json")
						.build();

				   // Read from request
				response = client.newCall(request).execute();
				String jsonData = response.body().string();
			    JSONObject Jobject = new JSONObject(jsonData);
			    
			     JSONArray result = Jobject.getJSONArray("records");
			     JSONObject fielsValue = result.getJSONObject(0);
			     ParticipantActionsID.put("pActionId0", fielsValue.get(queryField).toString()) ;
			     
//			     JSONObject fielsValue1 = result.getJSONObject(1);
//			     ParticipantActionsID.put("pActionId1", fielsValue1.get(queryField).toString());
//			     
//			     JSONObject fielsValue2 = result.getJSONObject(2);
//			     ParticipantActionsID.put("pActionId2", fielsValue2.get(queryField).toString());
//			     
//			     JSONObject fielsValue3 = result.getJSONObject(3);
//			     ParticipantActionsID.put("pActionId3", fielsValue3.get(queryField).toString());
//			     
//			     JSONObject fielsValue4 = result.getJSONObject(4);
//			     ParticipantActionsID.put("pActionId4", fielsValue4.get(queryField).toString());
			     
			     
				assertEquals(response.code(), 200);
				
			} catch (Exception e) {
				System.out.println("API error" + e.getMessage());
				Assert.fail();
			}
			return ParticipantActionsID;
			
			
		}
		
		public static void main (String [] args ) throws JSONException {
	   
					
				String token = getBearerToken("https://test.salesforce.com", "deepanker.acharya@metacube.com.gsautomat", "ebmdna0198");
				String firstname = "LeadFirst_".concat(HelperFunctions.GetRandomString(5));
				String lastname = "LeadLast_".concat(HelperFunctions.GetRandomString(5));
				String company = "Company_".concat(HelperFunctions.GetRandomString(5));
				String leadId = SalesForceAPIUtility.createLead(token, "https://ringdna--gsautomat--rdnacadence.visualforce.com/", firstname, lastname, company);
			//	String www = verifyLeadObjfield( token, "https://ringdna--gspatch.my.salesforce.com", leadId);
				}
 }