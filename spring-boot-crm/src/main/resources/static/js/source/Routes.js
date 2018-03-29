import React from 'react';
import {Router,Route,browerHistory} from 'react-router';

import Home from './pages/Home';

import About from './pages/Home';

const history=browerHistory;
const Routes=()=>(
  <Router history={browerHistory}>
    <Route path="home" component={Home}></Route>
    <Route path="about" component={About}></Route>
  </Router>
);
export default Routes;