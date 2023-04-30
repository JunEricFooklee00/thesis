def find_closest_employee(employeejob, UserLocation):  #

    # import libraries
    import pymongo  # database
    import numpy as np
    # VS-TFIDF
    from sklearn.feature_extraction.text import CountVectorizer, TfidfTransformer
    from sklearn.metrics.pairwise import cosine_similarity
    # Location
    # Location
    import googlemaps
    import dns.resolver
    dns.resolver.default_resolver = dns.resolver.Resolver(configure=False)
    dns.resolver.default_resolver.nameservers = ['8.8.8.8']

    # database connection
    client = pymongo.MongoClient("mongodb+srv://charlesadmin:admin123@employeemanagementsyste.yckarjq.mongodb.net/?retryWrites=true&w=majority")
    db = client['Users']
    collection = db['employees']

    # print received arguments for debugging purposes
    # print("Received arguments:")
    # print(sys.argv)

    # supposing this is the string that will find

    Find_Worker = str(employeejob)  # 'Plumber' #'' 'labor helper' employeejob
    Project_Location = str(UserLocation)  # 'Gen. T. De Leon, Valenzuela City, Metro Manila, Philippines' #String(UserLocation) #need to be sys.argv[3]
    profile = 'jobType'
    employee_profiles = collection.find({}, {profile: 1})

    job_profile_list = [r[profile] for r in employee_profiles]
    # Create the document-term matrix
    vectorizer = CountVectorizer()
    doc_term_matrix = vectorizer.fit_transform([Find_Worker] + job_profile_list)

    # Compute the TF-IDF scores
    tfidf_transformer = TfidfTransformer()
    tfidf_matrix = tfidf_transformer.fit_transform(doc_term_matrix)

    # Create the query vector
    query_vector = tfidf_matrix[0]

    # Compute the cosine similarity
    cosine_similarities = cosine_similarity(query_vector, tfidf_matrix[1:])[0]

    # Create a list to store the top matches
    top_matches = []
    gmaps = googlemaps.Client(key='AIzaSyCfwQxvXZrIM1V3LEREkp7po-yLCTRSMHc')

    # Set the number of top matches to return
    # num_matches = int(sys.argv[2])

    ## Compute the cosine similarity
    cosine_similarities = cosine_similarity(query_vector, tfidf_matrix[1:])[0]

    # Create a list to store the top matches
    top_matches = []
    gmaps = googlemaps.Client(key='AIzaSyCfwQxvXZrIM1V3LEREkp7po-yLCTRSMHc')

    # Retrieve the corresponding documents from the collection based on their index
    for i in np.argsort(cosine_similarities)[::-1]:
        if cosine_similarities[i] == 1:
            employee = collection.find_one(skip=int(i))
            # if employee['Availability'] == 'TRUE':
            # Get the employee's aaddress and calculate the distance to the project location
            employee_address = employee['address']
            distance_matrix = gmaps.distance_matrix(origins=employee_address,
                                                    destinations=Project_Location, mode='driving')
            distance = distance_matrix['rows'][0]['elements'][0]['distance']['text']
            employee['distance'] = distance
            top_matches.append(employee)

    # Sort the top matches based on distance and rating
    top_matches.sort(key=lambda x: (x['distance']))

    # Return all the top matches
    # print(top_matches)

    # Store the results in a JSON object
    results = []
    for employee in top_matches:
        result = {
            'avatar' : employee['avatar'],
            'employeeId' : employee['_id'],
            'name': employee['name'],
            'address': employee['address'],
            'jobType': employee['jobType'],
            # 'Availability': employee['Availability'],
            # 'Rating': employee['Rating'],
            'distance': employee['distance']
        }
        results.append(result)


    return results