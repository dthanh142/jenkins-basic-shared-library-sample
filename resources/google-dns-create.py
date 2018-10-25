import sys
from pprint import pprint

from googleapiclient import discovery
from oauth2client.client import GoogleCredentials

credentials = GoogleCredentials.get_application_default()

service = discovery.build('dns', 'v1', credentials=credentials)

# Identifies the project addressed by this request.
project = 'vndirect-compute'  # TODO: Update placeholder value.

# Identifies the managed zone addressed by this request. Can be the managed zone name or id.
managed_zone = 'vndirect-com-vn'  # TODO: Update placeholder value.

request_list = service.resourceRecordSets().list(project=project, managedZone=managed_zone)
while request_list is not None:
    response_list = request_list.execute()

    record_set_found = 0
    for resource_record_set in response_list['rrsets']:
        if resource_record_set['name'] == "{}".format(sys.argv[1]):
            pprint(resource_record_set)
            record_set_found += 1
            print "Record set already exists"
            break
    if record_set_found == 0:
        print "Record set not found"
	    print "Creating record set..."
        change_body = { 
            "additions": [
                {
                    "kind": "dns#resourceRecordSet",
                    "name": "{}".format(sys.argv[1]),
                    "rrdatas": [
                        "{}".format(sys.argv[2])
                    ],
                    "ttl": 300,
                    "type": "A"
                }
            ]
        }

        request_create = service.changes().create(project=project, managedZone=managed_zone, body=change_body)
        response_create = request_create.execute()
    
        # TODO: Change code below to process each `resource_record_set` resource:
        # pprint(resource_record_set)

    request_list = service.resourceRecordSets().list_next(previous_request=request_list, previous_response=response_list)
