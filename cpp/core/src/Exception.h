#ifndef __EXCEPTION_H__
#define __EXCEPTION_H__

/*
 *  Exception.h
 *  ZXing
 *
 *  Created by Christian Brunschen on 03/06/2008.
 *  Copyright 2008 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#include <string>

class Exception {
private:
  std::string message;
public:
  Exception(const char *msg) : message(msg) { }
  virtual const char* what() const throw() {
    return message.c_str();
  }
  virtual ~Exception() throw() { }
};

#endif // __EXCEPTION_H__