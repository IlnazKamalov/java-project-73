// @ts-check

import React from 'react';
import { Navbar as BootstrapNavbar, Container, Nav } from 'react-bootstrap';
import { useTranslation } from 'react-i18next';
import { Link, useHistory } from 'react-router-dom';

import { useAuth } from '../hooks/index.js';
import routes from '../routes.js';

const Navbar = () => {
  const { logOut, user } = useAuth();
  const history = useHistory();
  const { t } = useTranslation();

  const onLogout = () => {
    logOut();
    const from = { pathname: routes.homePagePath() };
    history.push(from, { message: 'logoutSuccess' });
  };

  return (
    <BootstrapNavbar bg="dark" variant="dark" className="mb-3">
      <Container fluid>
        <Nav className="me-auto">
          <Link className="nav-link" to={routes.homePagePath()}>{t('IlnazTodo')}</Link>
          <Link className="nav-link" to={routes.usersPagePath()}>{t('users')}</Link>
          {!!user && <Link className="nav-link" to={routes.statusesPagePath()}>{t('statuses')}</Link>}
          {!!user && <Link className="nav-link" to={routes.labelsPagePath()}>{t('labels')}</Link>}
          {!!user && <Link className="nav-link" to={routes.tasksPagePath()}>{t('tasks')}</Link>}
        </Nav>
        <Nav className="justify-content-end">
          {user ? (
            <Nav.Link onClick={() => onLogout()}>{t('logout')}</Nav.Link>
          ) : (
            <>
              <Link className="nav-link" to={routes.loginPagePath()}>{t('login')}</Link>
              <Link className="nav-link" to={routes.signupPagePath()}>{t('signup')}</Link>
            </>
          )}
        </Nav>
      </Container>
    </BootstrapNavbar>
  );
};

export default Navbar;
