"use server"

import { auth, signIn, signOut } from 'auth';

import {
  UserCircleIcon
} from '@heroicons/react/24/outline';

export default async function SignOut() {

  return (
    <>
        <form
          action={async () => {
            'use server';
            await signOut();
          }} >
          <button type="submit">
                <UserCircleIcon className="w-6" />
                <span className="hidden md:block">Sign out</span>
          </button>
        </form>

    </>
  );

}